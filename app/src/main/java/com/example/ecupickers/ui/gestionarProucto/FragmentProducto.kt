package com.example.ecupickers.ui.gestionarProucto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner

import com.example.ecupickers.R
import kotlinx.android.synthetic.main.fondo_productos.*
import kotlinx.android.synthetic.main.fondo_productos.view.*

class FragmentProducto : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_producto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var comboCategorias:Spinner = view.spinnerCategoriasProductos

        comboCategorias?.let {
            val adaptador: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(view.context,
                R.array.opciones_categorias, android.R.layout.simple_spinner_item)
            it.adapter=adaptador
        }
    }



}
