package com.example.ecupickers.modelos

data class MiembrosAlimentos constructor(
    var descripcion: String,
    var nombre: String,
    var precio: String,
    var id:String
) {


    public constructor() : this("def", "def", "def","def")
}