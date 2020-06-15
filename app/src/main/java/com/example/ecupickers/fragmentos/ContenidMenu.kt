package com.example.ecupickers.fragmentos

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView

import com.example.ecupickers.R
import com.example.ecupickers.constantes.EnumCamposDB
import com.example.ecupickers.constantes.EnumCategoria

import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.dialogs.DialogoAddProductosAlMenu
import com.example.ecupickers.dialogs.DialogoCartaAgregarProducto
import com.example.ecupickers.factory.DbReference

import com.example.ecupickers.modelos.MiembrosAlimentos
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.carta_add_productos_al_menu.view.cartaAddProductosAlMenu
import kotlinx.android.synthetic.main.carta_productos.view.*
import kotlinx.android.synthetic.main.recyclerview_carta_productos.view.*


class ContenidMenu : Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var idLocal: String
    private lateinit var idMenu: String
    private lateinit var opciones: FirebaseRecyclerOptions<MiembrosAlimentos>
    private lateinit var adapter: FirebaseRecyclerAdapter<MiembrosAlimentos, MenuViewHolder>
    private lateinit var reference: DatabaseReference
    private lateinit var qry: Query
    private lateinit var btnAddAlimentoAlMenu: ConstraintLayout


    companion object {
        var IDMENU: String = "idMenu"
        var IDLOCAL: String = "idLocal"
        var IDPRODUCTO:String="idProducto"
    }

    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titulo = view.txtVTituloCartaProducto!!
        var descripcion = view.txtcartaProductoDescripcion!!
        var precio = view.txtcartaProductoPrecio!!
        var opciones = view.btnEditarCartaProducto!!

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
        btnAddAlimentoAlMenu = root.cartaAddProductosAlMenu
        return root
    }

    override fun onStart() {
        super.onStart()
        adapter = gestionarRvProductos(qry)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter
        adapter.startListening()
        mostrarCarta(btnAddAlimentoAlMenu)
    }

    private fun gestionarRvProductos(qry: Query):
            FirebaseRecyclerAdapter<MiembrosAlimentos, MenuViewHolder> {
        opciones =
            FirebaseRecyclerOptions.Builder<MiembrosAlimentos>()
                .setQuery(qry, MiembrosAlimentos::class.java).build()
        return object : FirebaseRecyclerAdapter<MiembrosAlimentos, MenuViewHolder>(opciones) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.carta_productos, parent, false)
                return MenuViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: MenuViewHolder,
                position: Int,
                model: MiembrosAlimentos
            ) {

                val descripcion = model.descripcion
                val precio = model.precio
                val nombre = model.nombre
                val idProducto=model.id
                holder.descripcion.text = descripcion
                holder.precio.text = precio
                holder.titulo.text = nombre
                holder.opciones.setOnClickListener {
                    editarProducto(idProducto,idLocal)
                }
            }
        }
    }

    private fun editarProducto(idProducto:String,idLocal:String) {
        val bundle=Bundle()
        val window = DialogoCartaAgregarProducto(true)
        bundle.putString(IDPRODUCTO,idProducto)
        bundle.putString(IDLOCAL,idLocal)
        window.arguments=bundle
        window.show(childFragmentManager, "DialogoEditarProducto")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idMenu = it.getString(IDMENU)!!
            idLocal = it.getString(IDLOCAL)!!
        }
    }

    private fun mostrarCarta(view: View) {
        val window = DialogoAddProductosAlMenu()
        val bundle = Bundle()
        view.setOnClickListener {
            bundle.putString(IDLOCAL, idLocal)
            bundle.putString(IDMENU, idMenu)
            window.arguments = bundle
            window.show(childFragmentManager, "DialogoAddProductoAlMenu")
        }
    }


}
