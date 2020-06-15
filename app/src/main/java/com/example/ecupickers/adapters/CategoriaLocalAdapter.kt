package com.example.ecupickers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ecupickers.R
import com.example.ecupickers.clases.Locales
import com.example.ecupickers.constantes.EnumCategoria

import com.google.android.material.chip.Chip
import es.dmoral.toasty.Toasty

class CategoriaLocalAdapter(
    categorias: ArrayList<String>,
    ids: HashMap<String, String>

) :
    RecyclerView.Adapter<CategoriaLocalAdapter.CategoriaViewHolder>() {


    private lateinit var idLocal: String
    private lateinit var idCiudad: String
    private val categorias = categorias
    private var ids = ids



    inner class CategoriaViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {


        private var categoriaV: Chip = view.findViewById(R.id.btnCategoriaTituloCategoria)

        fun eliminarDelRv(position: Int, categoria: EnumCategoria) {
            categorias.remove(categoria.getCategoria())
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, categorias.size)
        }

        fun bind(categoriaP: String, position: Int) {
            ids?.let {
                if (it.contains("idLocal")) {
                    idLocal = it["idLocal"]!!
                }
                if (it.contains("ciudad")) {
                    idCiudad = it["ciudad"]!!
                }
            }
            categoriaV?.text = categoriaP

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
                                    localManager.gestionarCategoriaLocal(
                                        true,
                                        categoriasI,
                                        itLocal,
                                        itciudad
                                    )
                                    eliminarDelRv(position, categoriasI[0])
                                    Toasty.success(
                                        it.context,
                                        "La categoria:${categoria} se elimino de las categorias de local",
                                        Toast.LENGTH_LONG,
                                        true
                                    ).show()

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