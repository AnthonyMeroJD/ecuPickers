package com.example.ecupickers.clases

import com.example.ecupickers.interfaces.Pedido

class PedidoInsumo :Pedido{
    constructor(pedido:HashMap<String,Boolean>)
    override val local: String
        get() = TODO("Not yet implemented")
    override val cliente: String
        get() = TODO("Not yet implemented")
    override val fechaPedido: String
        get() = TODO("Not yet implemented")
    override val fechaEntrega: String
        get() = TODO("Not yet implemented")
    override val horaPedido: String
        get() = TODO("Not yet implemented")
    override val horaEntrega: String
        get() = TODO("Not yet implemented")
    override val estado: String
        get() = TODO("Not yet implemented")
    override val total: String
        get() = TODO("Not yet implemented")
    override val pendiente: String
        get() = TODO("Not yet implemented")

    override fun hacerPedido(pedido: Pedido): Boolean {
        TODO("Not yet implemented")
    }

}