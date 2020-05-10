package com.example.ecupickers.interfaces

import com.example.ecupickers.clases.Consumidor

interface Pedido {
    val local:String
    val cliente:String
    val fechaPedido:String
    val fechaEntrega:String
    val horaPedido:String
    val horaEntrega:String
    val estado:String
    val total:String
    val pendiente:String
    fun hacerPedido(pedido: Pedido):Boolean

}