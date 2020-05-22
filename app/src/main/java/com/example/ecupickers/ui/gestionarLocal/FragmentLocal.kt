package com.example.ecupickers.ui.gestionarLocal

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

import com.example.ecupickers.R
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Local
import com.example.ecupickers.modelos.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.carta_add_productos_menu_existente.view.*


class FragmentLocal : Fragment() {

    private lateinit var uid: String
    private lateinit var nombrelocal: EditText
    private lateinit var  ref:DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        ref= DbReference.getRef(EnumReferenciasDB.ROOT)

        return inflater.inflate(R.layout.fragment_local, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nombrelocal = view.findViewById(R.id.editText4)
        val qry = ref.child(EnumReferenciasDB.USERS.rutaDB()).orderByKey().equalTo(uid)
        qry.addChildEventListener(traerUser())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString("idLocal")!!
        }


    }

    fun traerUser(): ChildEventListener {
        var user = User()
        val listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                user = p0.getValue(User::class.java)!!

                traerLocal(p0.key.toString(), user.ciudad)

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }
        }
        return listener
    }

    fun traerLocal(idUser: String, ciudad: String) {
        val qry = ref.child(
            "${EnumReferenciasDB.MIEMBROSVENDEDORES.rutaDB()}" +
                    "/${ciudad}/${idUser}"
        )
        val listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                pintarDatosLocal(p0.key.toString())

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

        }
        qry.addChildEventListener(listener)
    }
    fun pintarDatosLocal(idLocal: String){
        val qry=ref.child("${EnumReferenciasDB.LOCALES.rutaDB()}").orderByKey().equalTo(idLocal)
        var local=Local()
        val listener=object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                local= p0.getValue(Local::class.java)!!
                nombrelocal.setText(local.nombre)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

        }
        qry.addChildEventListener(listener)
    }
}
