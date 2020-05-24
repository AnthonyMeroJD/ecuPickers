package com.example.ecupickers.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.ecupickers.R
import com.example.ecupickers.constantes.EnumCamposDB

import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Menu
import com.example.ecupickers.modelos.MiembrosAlimentos
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.carta_productos.view.*
import kotlinx.android.synthetic.main.recyclerview_carta_productos.view.*


class ContenidMenu : Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var idMenu: String
    private lateinit var opciones: FirebaseRecyclerOptions<MiembrosAlimentos>
    private lateinit var adapter: FirebaseRecyclerAdapter<MiembrosAlimentos, MenuViewHolder>
    private lateinit var reference: DatabaseReference
    private lateinit var qry: Query

    companion object {
        var IDMENU: String = "idMenu"
    }

    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titulo = view.txtVTituloCartaProducto
        var descripcion = view.txtcartaProductoDescripcion
        var precio = view.txtcartaProductoPrecio
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_contenid_menu, container, false)
        rv = root.rVCartasProductos
        reference = DbReference.getRef(EnumReferenciasDB.ROOT)
        qry =
            reference.child("${EnumReferenciasDB.MENUS.rutaDB()}/${idMenu}/${EnumCamposDB.MIEMBROSALIMENTOS.getCampos()}")

        opciones =
            FirebaseRecyclerOptions.Builder<MiembrosAlimentos>()
                .setQuery(qry, MiembrosAlimentos::class.java).build()
        adapter = object : FirebaseRecyclerAdapter<MiembrosAlimentos, MenuViewHolder>(opciones) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.carta_productos, parent, false)
                return MenuViewHolder(view)
            }

            override fun onBindViewHolder(holder: MenuViewHolder, position: Int, model: MiembrosAlimentos) {
                holder.descripcion.text=model.descripcion
                holder.precio.text=model.precio
                holder.titulo.text=model.nombre
            }
        }
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter
        return root
    }

    override fun onStart() {
        super.onStart()


        adapter.startListening()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idMenu = it.getString(IDMENU)!!
        }



    }


}
