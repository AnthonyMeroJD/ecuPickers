package com.example.ecupickers.modelos

data class Producto constructor(
    var nombre: String,
    var id:String,
    var precio: String,
    var descripcion: String,
    var calificacion: String?,
    var atendidos: String?,
    var local: String
) {
    public constructor() : this(
        "def","def","def", "def", "def", "def", "def"
    )
}