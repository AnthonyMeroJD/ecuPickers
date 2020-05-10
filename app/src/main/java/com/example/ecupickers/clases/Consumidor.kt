package com.example.ecupickers.clases

import android.app.DownloadManager
import com.example.ecupickers.constantes.EnumCiudad
import com.example.ecupickers.constantes.EnumEstado
import com.example.ecupickers.interfaces.Local
import com.example.ecupickers.interfaces.Pedido
import com.example.ecupickers.interfaces.User
import com.google.firebase.database.Query

class Consumidor:User {
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
    fun hacerPedido(pedido: Pedido){
        TODO("Not yet implemented")
    }
    fun pagarPedido(pedido: Pedido):EnumEstado{
        TODO("Not yet implemented")
    }
    fun puntuarServicio(local:Local):String{
        TODO("Not yet implemented")
    }
    fun puntuarProducto(pedido: Pedido):String{
        TODO("Not yet implemented")
    }
    fun traerLocal(ciudad:EnumCiudad):Query{
        TODO("Not yet implemented")
    }
    fun confirmarEntrega(pedido:Pedido):EnumEstado{
        TODO("Not yet implemented")
    }
    fun traerProducto(local: Local):Query{
        TODO("Not yet implemented")
    }
}