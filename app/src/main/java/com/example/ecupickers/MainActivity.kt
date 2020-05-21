package com.example.ecupickers

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.ecupickers.ui.gestionarLocal.FragmentLocal
import com.example.ecupickers.ui.gestionarProucto.FragmentProducto
import com.example.ecupickers.ui.inicio.FragmentInicio
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.prueba.*

class MainActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.fragmentLocal, R.id.fragmentProducto, R.id.nav_home), drawer_layout
        )
        nav_view.setupWithNavController(navController)
        var navigationView = nav_view
        //este es el escucha que que maneja la seleccion de los items
        var listener =NavigationView.OnNavigationItemSelectedListener { item ->
                var fragmentManager: FragmentManager = supportFragmentManager

                when (item.itemId) {
                    R.id.nav_home -> {
                        fragmentManager.beginTransaction().replace(
                            R.id.nav_host_fragment,
                            FragmentInicio()
                        ).commit()
                    }
                    R.id.nav_tus_productos -> {
                        fragmentManager.beginTransaction().replace(
                            R.id.nav_host_fragment,
                            FragmentProducto()
                        ).commit()
                    }
                    R.id.nav_tu_local-> {
                        fragmentManager.beginTransaction().replace(
                            R.id.nav_host_fragment,
                            FragmentLocal()
                        ).commit()
                    }
                }
                drawer_layout.closeDrawer(GravityCompat.START)
                true
            }
        navigationView.setNavigationItemSelectedListener(listener)

    }


}