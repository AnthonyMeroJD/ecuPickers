package com.example.ecupickers.modelos

data class Pedido constructor(
    var local: String,
    var cliente: String,
    var estado:String,
    var miembroProducto: HashMap<String, Boolean>
) {
    public constructor():this(
        "def","def","def",HashMap<String,Boolean>()
    )
}