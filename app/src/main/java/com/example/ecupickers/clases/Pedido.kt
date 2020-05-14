package com.example.ecupickers.clases

class Pedido {

    var miembroProducto:HashMap<String,Boolean>
        get() {
            miembroProducto?.let {
                return it
            }
        }
        set(value) {
            miembroProducto=value
        }
    var local:String
        get() {
            local?.let {
                return it
            }
        }
        set(value) {local=value}


    fun hacerPedido(pedido: Pedido): Boolean {
        TODO("Not yet implemented")
    }
}