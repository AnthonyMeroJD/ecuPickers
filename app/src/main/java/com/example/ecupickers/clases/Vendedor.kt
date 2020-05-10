package com.example.ecupickers.clases

import com.example.ecupickers.constantes.EnumCiudad
import com.example.ecupickers.constantes.EnumEstado
import com.example.ecupickers.interfaces.Local
import com.example.ecupickers.interfaces.User
import com.google.firebase.database.Query

class Vendedor:User {

    override val nombres: HashMap<String, String>
        get() = TODO("Not yet implemented")
    override val celular: String
        get() = TODO("Not yet implemented")
    override val ciudad: EnumCiudad
        get() = TODO("Not yet implemented")
    override val correo: String
        get() = TODO("Not yet implemented")
    override val clave: String
        get() = TODO("Not yet implemented")

    override fun registrarUser(): Boolean {
        TODO("Not yet implemented")
    }
    fun traerPedidos(local: Local):Query{
        TODO("Not yet implemented")
    }
    fun  traerPedidos(estado:EnumEstado,local:Local):Query{
        TODO("Not yet implemented")
    }


}