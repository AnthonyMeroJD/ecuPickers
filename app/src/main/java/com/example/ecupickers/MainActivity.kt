package com.example.ecupickers

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.ecupickers.constantes.EnumCategoria
import com.example.ecupickers.constantes.EnumCiudad
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.User
import com.example.ecupickers.ui.gestionarLocal.FragmentLocal
import com.example.ecupickers.ui.gestionarProucto.FragmentProducto
import com.example.ecupickers.ui.inicio.FragmentInicio
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fondo_productos.*
import kotlinx.android.synthetic.main.prueba.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var global: String
    private lateinit var bundle: Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.fragmentLocal, R.id.fragmentProducto, R.id.nav_home), drawer_layout
        )
        nav_view.setupWithNavController(navController)
        var navigationView = nav_view
        var fragment: Fragment
        bundle = Bundle()

        //aqui pasaremos los datos


        //este es el escucha que que maneja la seleccion de los items

        var listener = NavigationView.OnNavigationItemSelectedListener { item ->
            var fragmentManager: FragmentManager = supportFragmentManager

            when (item.itemId) {
                R.id.nav_home -> {
                    fragment = FragmentInicio()
                    fragmentManager.beginTransaction().replace(
                        R.id.nav_host_fragment,
                        fragment
                    ).commit()
                }
                R.id.nav_tus_productos -> {
                    fragment = FragmentProducto()
                    fragmentManager.beginTransaction().replace(
                        R.id.nav_host_fragment,
                        fragment
                    ).commit()
                }
                R.id.nav_tu_local -> {
                    fragment = FragmentLocal()
                    bundle.putString("idUser", auth.uid)
                    //bundle.putString("idUser", "Y0fPjvAGdaUDQMOAH3Fa80o2Cps2")
                    fragment.arguments = bundle
                    if (bundle.containsKey("idUser") && bundle.containsKey("ciudad")) {
                        fragmentManager.beginTransaction().replace(
                            R.id.nav_host_fragment,
                            fragment
                        ).commit()
                    }
                }
                R.id.nav_cerrar_secion -> {
                    auth.signOut()
                    var intent = Intent(this, IngresoActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
        navigationView.setNavigationItemSelectedListener(listener)


    }

    override fun onStart() {
        super.onStart()
        var qry =
            ref.child(EnumReferenciasDB.USERS.rutaDB()).orderByKey().equalTo(auth.uid)
        //var qry =
        //   ref.child(EnumReferenciasDB.USERS.rutaDB()).orderByKey().equalTo("Y0fPjvAGdaUDQMOAH3Fa80o2Cps2")
        qry.addChildEventListener(traerUsuario())
    }

    fun grabar(string: String) {
         bundle.putString("ciudad", string)
       // bundle.putString("ciudad", "Quito")
        Toast.makeText(this@MainActivity, string, Toast.LENGTH_SHORT).show()
    }

    fun traerUsuario(): ChildEventListener {


        return object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                var p = p0.getValue(User::class.java)
                grabar(p!!.ciudad!!)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                var p = p0.getValue(User::class.java)
                //   Toast.makeText(this@MainActivity, p!!.ciudad, Toast.LENGTH_SHORT).show()
                grabar(p!!.ciudad!!)

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

        }


    }
}