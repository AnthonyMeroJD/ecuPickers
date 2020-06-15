package com.example.ecupickers.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecupickers.R
import com.example.ecupickers.adapters.CategoriasAdapter
import com.example.ecupickers.fragmentos.ContenidMenu
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.carta_add_productos_menu_existente.view.*

class DialogoAddProductosAlMenu : BottomSheetDialogFragment() {
    private lateinit var touchListener: View.OnTouchListener
    private lateinit var rvCategoria: RecyclerView
    private lateinit var rvProducto: RecyclerView
    private lateinit var rvElegidos: RecyclerView
    private lateinit var btnAgregar: Button
    private lateinit var idMenu: String
    private lateinit var idLocal: String

    @SuppressLint("ClickableViewAccessibility")
    companion object {


        var IDMENU: String = "idMenu"
        var IDLOCAL: String = "idLocal"
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(
            R.layout.carta_add_productos_menu_existente,
            container, false
        )
        touchListener = View.OnTouchListener { v, event ->
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    v!!.parent.requestDisallowInterceptTouchEvent(true)

                }
                MotionEvent.ACTION_UP -> {
                    v!!.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            v.onTouchEvent(event)
            true;
        }
        rvCategoria = root.listViewCategoriaMostrar
        rvProducto = root.listViewProductosAñadir
        rvElegidos = root.listViewProductosSeleccionados
        btnAgregar = root.btnCartaaddProductosMenuExistente
        return root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        val categoria = arrayListOf(
            "Almuerzos",
            "Desayunos",
            "Meriendas",
            "Postres",
            "BBQ",
            "Comida Rapida",
            "Mariscos",
            "Pollos",
            "Helados",
            "Hamburguesas",
            "Pizzas"
        )
        arguments?.let {
            if (it.containsKey(IDLOCAL) && it.containsKey(IDMENU)) {
                idLocal=it.getString(IDLOCAL)!!
                idMenu=it.getString(IDMENU)!!
            }
        }
        if (checkLateVar()) {
            rvCategoria.apply {
                layoutManager = GridLayoutManager(context, 3)
                setOnTouchListener(touchListener)
                val ids = HashMap<String, String>()
                ids[ContenidMenu.IDMENU] = idMenu
                ids[ContenidMenu.IDLOCAL] = idLocal
                adapter = CategoriasAdapter(categoria,true,rvProducto,rvElegidos,idLocal,idMenu,btnAgregar)
            }
            rvElegidos.setOnTouchListener(touchListener)
            rvProducto.setOnTouchListener(touchListener)
        }
    }

    private fun checkLateVar(): Boolean {
        return this::idMenu.isInitialized && this::idLocal.isInitialized
    }
}