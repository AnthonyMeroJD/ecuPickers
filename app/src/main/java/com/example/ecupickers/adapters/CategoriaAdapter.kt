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
import com.example.ecupickers.clases.Productos
import com.example.ecupickers.constantes.EnumCamposDB
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.factory.DbReference

import com.example.ecupickers.fragmentos.ContenidMenu
import com.example.ecupickers.modelos.Alimento
import com.example.ecupickers.modelos.MiembrosAlimentos
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.botones_categoria.view.*

class CategoriaAdapter(
    categorias: ArrayList<String>,
    elegible: Boolean = false,
    rv: RecyclerView? = null,
    rvElegidos: RecyclerView? = null,
    btnAgregar: Button? = null,
    ids: HashMap<String, String>? = null
) :
    RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {


    private val categorias = categorias
    private var elegible = elegible
    private val rv = rv
    private val rvElegidos = rvElegidos
    private var productosElegidos = HashMap<String, String>()
    private var btnAgregar = btnAgregar
    private var ids = ids
    private var idsProductos =
        HashMap<String, HashMap<EnumCamposDB, String>>()

    inner class MiembroAlimentoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var categoriaV: TextView = view.btnCategoriaTituloCategoria
    }

    inner class CategoriaViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {


        private var categoriaV: TextView = view.findViewById(R.id.btnCategoriaTituloCategoria)


        fun bind(categoriaP: String) {
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
        holder.bind(categoria)
    }
}