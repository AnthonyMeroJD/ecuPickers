package com.example.ecupickers

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecupickers.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_inicar_sesion.*

class IngresoActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btnIniciarSecion: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_inicar_sesion)
        auth = FirebaseAuth.getInstance()
        btnIniciarSecion = buttonIniciarSesion
        email = editTextNombreUsuarioInicarSesion
        password = editTextPasswordInicioSesion
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
        var campos = arrayListOf<EditText>(email, password)
        btnIniciarSecion.setOnClickListener {

            if (comprobarCampos(campos)) {
                var sEmail = email.text.toString().trim().toLowerCase()
                var sPassword = password.text.toString()
                iniciarSecion(sEmail, sPassword)
            } else {
                Toasty.error(
                    applicationContext,
                    "no se a podido iniciar sesion .", Toast.LENGTH_SHORT, true
                ).show()
            }
        }
    }

    fun comprobarCampos(arrayList: ArrayList<EditText>): Boolean {
        var validar = true
        for (campo in arrayList) {
            if (campo.text.isBlank()) {
                campo.error = "compruebe que el campo este lleno correctamente"
                validar = false
            }
        }
        return validar
    }

    fun iniciarSecion(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // If sign in fails, display a message to the user.
                Toasty.error(
                    applicationContext,
                    "no se a podido iniciar sesion .", Toast.LENGTH_SHORT, true
                ).show()
            }
        }

    }

    private fun updateUI(user: FirebaseUser?) {
        //comprueba si el usuario ya esta autentificado

        if (user != null) {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}