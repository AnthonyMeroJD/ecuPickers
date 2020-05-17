package com.example.ecupickers.ui.gestionarProucto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecupickers.R

import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.botones_categoria.view.*

class CartaAgregarProducto : Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var rutaReference: DatabaseReference
    private lateinit var opciones: FirebaseRecyclerOptions<CategoriaRestaurante>
    private lateinit var adapter: FirebaseRecyclerAdapter<CategoriaRestaurante, CategoriasViewHolder>

    inner class CategoriasViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloCategoria = view.btnCategoriaTituloCategoria
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.carta_agregar_producto, container, false)
        rv = root.findViewById(R.id.rvCategorias)
        rutaReference=FirebaseDatabase.getInstance().reference.child("Categorias").child("categoriaRestaurante")
        return root
    }

    override fun onStart() {
        super.onStart()
        rv.layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false)
        val qry=rutaReference.limitToFirst(1)
        opciones = FirebaseRecyclerOptions.Builder<CategoriaRestaurante>()
            .setQuery(qry, CategoriaRestaurante::class.java).build()
        adapter =
            object : FirebaseRecyclerAdapter<CategoriaRestaurante, CategoriasViewHolder>(opciones) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): CategoriasViewHolder {
                    val view:View= LayoutInflater.from(parent.context).inflate(R.layout.botones_categoria,parent,false)
                    return CategoriasViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: CategoriasViewHolder,
                    position: Int,
                    model: CategoriaRestaurante
                ) {

                    for (cat in model.categoria.keys){
                        holder.tituloCategoria.text=cat
                    }
                }

            }
    }


}
