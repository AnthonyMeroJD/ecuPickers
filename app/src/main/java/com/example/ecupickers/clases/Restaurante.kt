package com.example.ecupickers.clases

import com.example.ecupickers.interfaces.Local

class Restaurante:Local {
    var tiposComida=HashMap<String,Boolean>()
    var menus=HashMap<String,Boolean>()
    var alimentos=HashMap<String,Boolean>()

    override val nombre: String
        get() = TODO("Not yet implemented")
    override val servicio: String
        get() = TODO("Not yet implemented")
    override val dueño: String
        get() = TODO("Not yet implemented")
    override val ciudad: String
        get() = TODO("Not yet implemented")
    override val calificacion: String
        get() = TODO("Not yet implemented")
    override val direccion: String
        get() = TODO("Not yet implemented")

    override fun registrarLocal(local: Local): Boolean {
        TODO("Not yet implemented")
    }

    override fun traerLocal(vendedor: Vendedor): ArrayList<Local> {
        TODO("Not yet implemented")
    }
}