package com.example.ecupickers.modelos

data class Producto constructor(
    var nombre: String,
    var precio: String,
    var descripcion: String,
    var calificacion: String,
    var local: String
) {
    public constructor() : this(
        "def", "def", "def", "def", "def"
    )
}