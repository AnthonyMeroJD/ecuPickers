package com.example.ecupickers.modelos

data class Restaurante constructor(
    var nombre: String,
    var estado:String,
    var horaInicio: String,
    var horaCierre: String,
    var miembrosCategorias: HashMap<String, Boolean>
) {
    public constructor() : this(
        "def","def", "def", "def", HashMap<String, Boolean>()
    )
}