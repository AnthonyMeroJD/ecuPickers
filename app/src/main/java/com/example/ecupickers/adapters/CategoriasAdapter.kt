package com.example.ecupickers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecupickers.R
import com.example.ecupickers.clases.Productos
import com.example.ecupickers.constantes.EnumCamposDB
import com.example.ecupickers.constantes.EnumCategoria
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Alimento
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.chip.Chip
import com.google.firebase.database.Query
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.botones_categoria.view.*

class CategoriasAdapter(
    categorias: ArrayList<String>,
    elegible: Boolean? = null,
    rvProducto: RecyclerView? = null,
    rvElegidos: RecyclerView? = null,
    idLocal: String? = null,
    idMenu: String? = null,
    btnAgregar: Button? = null
) :
    RecyclerView.Adapter<CategoriasAdapter.CategoriasViewHolder>() {


    private val categorias = categorias
    private val elegible = elegible
    private val rvProducto = rvProducto
    private val rvElegidos = rvElegidos
    private val idLocal = idLocal
    private val idMenu = idMenu
    private val btnAgregar = btnAgregar
    private var productosElegidosMap = HashMap<String, Boolean>()
    private var productosElegidosLista = ArrayList<String>()
    private var productosElegidosIdLista = ArrayList<String>()
    private var idsProductos =
        HashMap<String, HashMap<EnumCamposDB, String>>()
    private lateinit var adapterProductos: ProductosAdapter

    inner class MiembroAlimentoViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        var categoriaV: Chip = view.btnCategoriaTituloCategoria
    }

    inner class CategoriasViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var categoriaV: Chip = view.findViewById(R.id.btnCategoriaTituloCategoria)

        fun eliminarDelRv(position: Int, categoria: String) {
            categorias.remove(categoria)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, categorias.size)
        }

        fun bind(categoriaP: String, position: Int) {
            categoriaV.text = categoriaP
            elegible?.let {
                if (it) {
                    categoriaV.isCloseIconVisible = false
                    categoriaV.setOnClickListener {
                        rvProducto?.let { rvProducto ->
                            val reference = DbReference.getRef(EnumReferenciasDB.ROOT)
                            val qry = reference.child(
                                "${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/" +
                                        "$idLocal"
                            ).orderByChild(
                                "${EnumCamposDB.MIEMBROSCATEGORIAS.getCampos()}/" +
                                        "${categoriaP.replace(" ", "")}"
                            )
                                .equalTo(true)
                            val adapter = llenarRecyclerProductosElegibles(qry)
                            rvProducto.apply {
                                this.layoutManager = GridLayoutManager(context, 2)
                                this.adapter = adapter
                            }
                            adapter.startListening()
                        }
                    }
                    btnAgregar?.let { btnAgragar ->
                        btnAgragar.setOnClickListener { v ->
                            agregarProductoAmenu(v)
                        }
                    }
                }
            }
            categoriaV.setOnCloseIconClickListener {
                eliminarDelRv(position, categoriaP)
            }
        }
    }

    private fun agregarProductoAmenu(view: View) {
        if (idsProductos.isEmpty()) {
            Toasty.warning(view.context, "Debe elegir un producto por lo menos", Toast.LENGTH_SHORT)
                .show()
        } else {
            var productoManager = Productos()
            productoManager.añadirProductosAMenu(idMenu!!, idsProductos, idLocal!!)
            Toasty.success(view.context, "Se añadio con exito los productos", Toast.LENGTH_SHORT)
                .show()

        }
    }

    private fun llenarRecyclerProductosElegibles(qry: Query): FirebaseRecyclerAdapter<Alimento, MiembroAlimentoViewHolder> {
        val opciones: FirebaseRecyclerOptions<Alimento> =
            FirebaseRecyclerOptions.Builder<Alimento>().setQuery(
                qry, Alimento::class.java
            ).build()
        return object :
            FirebaseRecyclerAdapter<Alimento, MiembroAlimentoViewHolder>(
                opciones
            ) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): MiembroAlimentoViewHolder {
                val view: View =
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.botones_categoria,
                        parent,
                        false
                    )
                return MiembroAlimentoViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: MiembroAlimentoViewHolder,
                position: Int,
                model: Alimento
            ) {
                holder.categoriaV.isCloseIconVisible = false
                holder.categoriaV.text = model.nombre
                holder.categoriaV.setOnClickListener {
                    elegirProducto(model)
                }
            }

        }

    }

    private fun elegirProducto(model: Alimento) {
        rvElegidos?.let { rvElegidos ->
            val campos = HashMap<EnumCamposDB, String>()
            campos[EnumCamposDB.NOMBRE] = model.nombre
            campos[EnumCamposDB.DESCRIPCION] = model.descripcion
            campos[EnumCamposDB.PRECIO] = model.precio
            addProductoElegidosAlista(model.id, model.nombre, campos)

            adapterProductos = ProductosAdapter(
                productosElegidosLista, productosElegidosIdLista, idsProductos
            )
            rvElegidos.apply {
                this.layoutManager = GridLayoutManager(
                    context,
                    3
                )
                this.adapter = adapterProductos
            }
        }
    }

    private fun addProductoElegidosAlista(
        id: String,
        nombre: String,
        menuCampos: HashMap<EnumCamposDB, String>
    ) {
        if (checklatevar()) {
            productosElegidosLista = adapterProductos.getProductos()
            productosElegidosIdLista = adapterProductos.getIdsProductos()
            idsProductos = adapterProductos.getProductosElegidosMap()
            for (producto in productosElegidosMap) {
                productosElegidosMap[producto.key] = false
            }
            for (producto in productosElegidosLista) {
                if (productosElegidosMap.containsKey(producto)) {
                    productosElegidosMap[producto] = true
                }
            }
            if (!productosElegidosLista.contains(nombre)) {
                productosElegidosLista.add(nombre)
                productosElegidosIdLista.add(id)
                idsProductos[id] = menuCampos
            }
        } else {
            if (!productosElegidosLista.contains(nombre)) {
                productosElegidosLista.add(nombre)
                productosElegidosIdLista.add(id)
                idsProductos[id] = menuCampos
            }
        }
    }

    private fun checklatevar(): Boolean {
        return this::adapterProductos.isInitialized
    }

    fun getCategorias(): ArrayList<String> {
        return categorias
    }

    override fun getItemCount(): Int = categorias.size
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriasAdapter.CategoriasViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.botones_categoria, parent, false)
        return CategoriasViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriasAdapter.CategoriasViewHolder, position: Int) {
        val categoria: String = categorias[position]
        holder.bind(categoria, position)
    }
}