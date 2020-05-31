package com.example.ecupickers.modelos

data class Restaurante constructor(
    var nombre: String,
    var estado:String,
    var id:String,
    var horaInicio: String,
    var horaCierre: String,
    var miembroscategorias: HashMap<String, Boolean>
) {
    public constructor() : this("def",
        "def","def", "def", "def", HashMap<String, Boolean>()
    )
}