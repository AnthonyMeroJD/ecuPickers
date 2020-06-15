package com.example.ecupickers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecupickers.R
import com.example.ecupickers.constantes.EnumCamposDB
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.botones_categoria.view.*

class ProductosAdapter(
    productosElegidos: ArrayList<String>, productosElegidosIds: ArrayList<String>,
                        productosElegidosMap:HashMap<String,HashMap<EnumCamposDB,String>>
    ) :
    RecyclerView.Adapter<ProductosAdapter.ProductosViewHolder>() {
    private var productosElegidosMap=productosElegidosMap
    private var productosElegidos = productosElegidos
    private var productosElegidosIds = productosElegidosIds

    inner class ProductosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var productoC: Chip = view.btnCategoriaTituloCategoria
        fun eliminarDelRv(position: Int, producto: String) {
            productosElegidos.remove(producto)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, productosElegidos.size)
        }
        fun eliminarId(position: Int){
            productosElegidosMap.remove(productosElegidosIds[position])
            productosElegidosIds.removeAt(position)
        }
        fun bind(producto: String,position: Int) {
            productoC.text = producto
            productoC.setOnCloseIconClickListener {
                eliminarDelRv(position,producto)
                eliminarId(position)
            }
        }
    }

    override fun getItemCount(): Int = productosElegidos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductosViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.botones_categoria, parent, false)
        return ProductosViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductosViewHolder, position: Int) {
        val producto = productosElegidos[position]
        holder.bind(producto,position)
    }

    fun getProductos(): ArrayList<String> {
        return productosElegidos
    }

    fun getIdsProductos(): ArrayList<String> {
        return productosElegidosIds
    }
    fun getProductosElegidosMap():HashMap<String,HashMap<EnumCamposDB,String>>{
        return productosElegidosMap
    }

}