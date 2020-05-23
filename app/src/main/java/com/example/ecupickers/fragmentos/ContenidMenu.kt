package com.example.ecupickers.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.ecupickers.R
import kotlinx.android.synthetic.main.recyclerview_cartas_inicio_pedidos.view.*


class ContenidMenu : Fragment() {

    private lateinit var rv: RecyclerView
    private lateinit var idMenu: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      val root =inflater.inflate(R.layout.fragment_contenid_menu, container, false)
       rv=root.rVCartasPedidosInicio
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


}
