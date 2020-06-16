package com.example.ecupickers.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.ecupickers.R.layout.carta_nuevo_menu
import com.example.ecupickers.clases.Locales
import com.example.ecupickers.clases.Menus
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.carta_nuevo_menu.view.*
import org.w3c.dom.Text

class DialogoCrearMenu(editar: Boolean) : BottomSheetDialogFragment() {
    private lateinit var btnGuardar: Button
    private lateinit var nombreMenu: EditText
    private lateinit var idLocal: String
    private lateinit var idMenu: String
    private val editar = editar

    companion object {
        var IDLOCAL: String = "idLocal"
        var IDMENU: String = "idMenu"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(carta_nuevo_menu, container, false)
        btnGuardar = root.btnCartanuevoMenuAgregarMenu
        nombreMenu = root.txtNombreMenu
        return root
    }

    override fun onStart() {
        super.onStart()
        arguments?.let {
            if (it.containsKey(IDLOCAL)) {
                idLocal = it.getString(IDLOCAL)!!
            }
            if (it.containsKey(IDMENU)){
                idMenu=it.getString(IDMENU)!!
            }
        }
        if (checkIdLocal()) {
            if (editar && checkIdLocal() && checkIdMenu()) {
                btnGuardar.setOnClickListener { editarMenu() }
            } else {
                btnGuardar.setOnClickListener { guardarMenu() }
            }
        }
    }

    private fun checkIdLocal(): Boolean {
        return this::idLocal.isInitialized
    }

    private fun checkIdMenu(): Boolean {
        return this::idMenu.isInitialized
    }

    private fun editarMenu() {
        val menuManager = Menus()
        if (nombreMenu.text.isEmpty() || nombreMenu.text.isBlank()) {
            nombreMenu.error = "este campo no puede estar vacio"
            Toasty.warning(
                requireContext(),
                "El nombre del menu no puede estar en balnco",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            menuManager.gestionarNombre(nombreMenu.text.toString(), idLocal, idMenu)
            Toasty.success(
                requireContext(),
                "El nombre del menu a cambiado",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun guardarMenu() {
        val localManager = Locales()
        if (nombreMenu.text.isBlank() || nombreMenu.text.isEmpty()) {
            Toasty.error(
                requireContext(), "El nombre del menu no puede estar vacio",
                Toast.LENGTH_SHORT
            ).show()
            nombreMenu.error = "este campo es obligatorio"
        } else {
            localManager.gestionarMenu(null, nombreMenu.text.toString(), idLocal)
            Toasty.success(
                requireContext(), "Se agrego el menu ${nombreMenu.text} a tu local",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}