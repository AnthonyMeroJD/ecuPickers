package com.example.ecupickers.modelos

data class Pedido constructor(
    var local: String,
    val id:String,
    var cliente: String,
    var estado: String,
    var fecha: String,
    var horaPedido: String,
    var horaEntrega: String?,
    var direccionEntrega:String?,
    var miembrosProductos: HashMap<String, HashMap<String,String>>
) {
    public constructor() : this(
        "def", "def", "def", "def","def","def",
        "def", "def", HashMap<String, HashMap<String,String>>()
    )
}