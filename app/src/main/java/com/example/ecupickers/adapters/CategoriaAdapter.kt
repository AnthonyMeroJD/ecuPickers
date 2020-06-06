package com.example.ecupickers.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecupickers.R
import com.example.ecupickers.clases.Locales
import com.example.ecupickers.clases.Productos
import com.example.ecupickers.constantes.EnumCamposDB
import com.example.ecupickers.constantes.EnumCategoria
import com.example.ecupickers.constantes.EnumCiudad
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.factory.DbReference

import com.example.ecupickers.fragmentos.ContenidMenu
import com.example.ecupickers.modelos.Alimento
import com.example.ecupickers.modelos.MiembrosAlimentos
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.chip.Chip
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.botones_categoria.view.*
import kotlinx.android.synthetic.main.layout_perfil.view.*

class CategoriaAdapter(
    categorias: ArrayList<String>,
    elegible: Boolean = false,
    rv: RecyclerView? = null,
    rvElegidos: RecyclerView? = null,
    btnAgregar: Button? = null,
    ids: HashMap<String, String>? = null,
    idLocal: String? = null,
    idCiudad: String? = null,
    global:Boolean?=null
) :
    RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {


    private val idLocal = idLocal
    private val idCiudad = idCiudad
    private val categorias = categorias
    private var elegible = elegible
    private val rv = rv
    private val rvElegidos = rvElegidos
    private var productosElegidos = HashMap<String, String>()
    private var btnAgregar = btnAgregar
    private var ids = ids
    private var idsProductos =
        HashMap<String, HashMap<EnumCamposDB, String>>()
    private val global=global
    //esta clase se usa en el rv interno, se lo ocupa en rv:Firebase si elegible es true
    inner class MiembroAlimentoViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        var categoriaV: TextView = view.btnCategoriaTituloCategoria
    }

    inner class CategoriaViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {


        private var categoriaV: Chip = view.findViewById(R.id.btnCategoriaTituloCategoria)

        fun eliminarDelRv(position: Int,categoria:EnumCategoria){
            categorias.remove(categoria.getCategoria())
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, categorias.size)
        }
        fun bind(categoriaP: String, position: Int) {
            categoriaV?.text = categoriaP

            if (elegible) {
                categoriaV.setOnClickListener {
                    rv?.let {
                        var opciones: FirebaseRecyclerOptions<Alimento>
                        var adapter: FirebaseRecyclerAdapter<Alimento, MiembroAlimentoViewHolder>
                        var reference = DbReference.getRef(EnumReferenciasDB.ROOT)
                        var qry = reference.child(
                            "${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/" +
                                    "-M81FWFVsd4dwkXyiCAk"
                        ).orderByChild(
                            "${EnumCamposDB.MIEMBROSCATEGORIAS.getCampos()}/" +
                                    "${categoriaP.replace(" ", "")}"
                        )
                            .equalTo(true)
                        opciones = FirebaseRecyclerOptions.Builder<Alimento>().setQuery(
                            qry, Alimento::class.java
                        ).build()
                        adapter = object :
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
                                holder.categoriaV.text = model.nombre
                                holder.categoriaV.setOnClickListener {
                                    rvElegidos?.let { it1 ->
                                        var listaElegidos = ArrayList<String>()
                                        productosElegidos.put(model.id, model.nombre)
                                        for (producto in productosElegidos) {
                                            listaElegidos.add(producto.value)
                                        }
                                        var campos = HashMap<EnumCamposDB, String>()
                                        campos.put(EnumCamposDB.NOMBRE, model.nombre)
                                        campos.put(EnumCamposDB.DESCRIPCION, model.descripcion)
                                        campos.put(EnumCamposDB.PRECIO, model.precio)
                                        idsProductos.put(model.id, campos)

                                        it1.apply {
                                            this.layoutManager = LinearLayoutManager(context)
                                            this.adapter = CategoriaAdapter(listaElegidos)
                                        }
                                    }
                                }
                                btnAgregar?.let {
                                    it.setOnClickListener {
                                        if (productosElegidos.isNotEmpty()) {
                                            if (ids!!.contains("idMenu") &&
                                                ids!!.containsKey("idLocal")
                                            ) {
                                                var productoManager = Productos()
                                                productoManager.añadirProductosAMenu(
                                                    ids!!["idMenu"]!!,
                                                    idsProductos,
                                                    ids!!["idLocal"]!!
                                                )
                                                Toasty.success(
                                                    it.context,
                                                    "Se agrego con exito los productos al" +
                                                            " menu.",
                                                    Toast.LENGTH_SHORT,
                                                    true
                                                ).show()
                                            }
                                        }
                                    }

                                }
                            }

                        }
                        it.apply {
                            this.layoutManager = LinearLayoutManager(categoriaV.context)
                            this.adapter = adapter
                        }
                        adapter.startListening()
                    }
                }
            } else {
                categoriaV?.let {
                    fun categoriza(categoria: String): ArrayList<EnumCategoria> {
                        var categorias = ArrayList<EnumCategoria>()
                        when (categoria) {
                            EnumCategoria.ALMUERZO.getCategoria() -> categorias.add(EnumCategoria.ALMUERZO)
                            EnumCategoria.BBQ.getCategoria() -> categorias.add(EnumCategoria.BBQ)
                            EnumCategoria.COMIDARAPIDA.getCategoria() -> categorias.add(
                                EnumCategoria.COMIDARAPIDA
                            )
                            EnumCategoria.HAMBURGUESAS.getCategoria() -> categorias.add(
                                EnumCategoria.HAMBURGUESAS
                            )
                            EnumCategoria.HELADOS.getCategoria() -> categorias.add(EnumCategoria.HELADOS)
                            EnumCategoria.MARISCOS.getCategoria() -> categorias.add(EnumCategoria.MARISCOS)
                            EnumCategoria.MERIENDAS.getCategoria() -> categorias.add(EnumCategoria.MERIENDAS)
                            EnumCategoria.PIZZAS.getCategoria() -> categorias.add(EnumCategoria.PIZZAS)
                            EnumCategoria.POLLOS.getCategoria() -> categorias.add(EnumCategoria.POLLOS)
                            EnumCategoria.POSTRES.getCategoria() -> categorias.add(EnumCategoria.POSTRES)
                            EnumCategoria.DESAYUNOS.getCategoria() -> categorias.add(EnumCategoria.DESAYUNOS)
                        }
                        return categorias
                    }

                    var categoria = it.text.toString().replace(" ", "")
                    var categoriasI = categoriza(categoria)
                    var localManager = Locales()
                    it.setOnCloseIconClickListener {
                        idLocal?.let { itLocal ->
                            idCiudad?.let { itciudad ->
                                    if (global == true) {
                                        localManager.gestionarCategoriaLocal(
                                            true,
                                            categoriasI,
                                            itLocal,
                                            itciudad
                                        )

                                        eliminarDelRv(position,categoriasI[0])
                                        Toasty.success(
                                            it.context,
                                            "La categoria:${categoria} se elimino de las categorias de local",
                                            Toast.LENGTH_LONG,
                                            true
                                        ).show()
                                    }else{
                                        eliminarDelRv(position,categoriasI[0])
                                    }




                            }
                        }
                    }

                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.botones_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun getItemCount(): Int = categorias.size

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria: String = categorias[position]

        holder.bind(categoria, position)
    }
}