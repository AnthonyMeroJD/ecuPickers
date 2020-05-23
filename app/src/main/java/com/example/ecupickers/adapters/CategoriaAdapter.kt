package com.example.ecupickers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecupickers.R

class CategoriaAdapter(categorias: ArrayList<String>) :
    RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {
    private val categorias=categorias

    inner class CategoriaViewHolder(view:View) :
        RecyclerView.ViewHolder( view) {
        private var categoriaV: TextView=view.findViewById(R.id.btnCategoriaTituloCategoria)



        fun bind(categoriaP: String) {
            categoriaV?.text = categoriaP
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).
            inflate(R.layout.botones_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun getItemCount(): Int =categorias.size

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria:String = categorias[position]
        holder.bind(categoria)
    }
}