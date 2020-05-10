package com.example.ecupickers.interfaces

import com.example.ecupickers.constantes.EnumCiudad

interface User {
    val nombres:HashMap<String,String>
    val celular:String
    val ciudad:String
    val correo:String
    val clave:String
    fun registrarUser():Boolean
}