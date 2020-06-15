package com.example.ecupickers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecupickers.R
import com.google.android.material.chip.Chip

class MenusAdapter(menus:ArrayList<String>):
    RecyclerView.Adapter<MenusAdapter.MenusViewHolder>() {
    private val menus=menus


    inner class  MenusViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var categoriaV: Chip = view.findViewById(R.id.btnCategoriaTituloCategoria)

        fun eliminarDelRv(position: Int, categoria:String) {
            menus.remove(categoria)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menus.size)
        }

        fun bind(categoriaP: String, position: Int) {
            categoriaV.text = categoriaP
            categoriaV.setOnCloseIconClickListener {
                eliminarDelRv(position,categoriaP)
            }
        }
    }

    override fun getItemCount(): Int {
      return menus.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenusAdapter.MenusViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.botones_categoria, parent, false)
        return MenusViewHolder(view)
    }


    override fun onBindViewHolder(holder: MenusViewHolder, position: Int) {
        val menu: String = menus[position]
        holder.bind(menu, position)
    }

    fun getMenus():ArrayList<String>{
        return menus
    }
}