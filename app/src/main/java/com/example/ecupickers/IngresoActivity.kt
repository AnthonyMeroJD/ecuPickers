package com.example.ecupickers

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecupickers.R
import kotlinx.android.synthetic.main.layout_inicar_sesion.*

class IngresoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_inicar_sesion)
        buttonIniciarSesion.setOnClickListener{
            var intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }
}