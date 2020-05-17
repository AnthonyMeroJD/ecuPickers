package com.example.ecupickers.modelos

data class Restaurante constructor(
    var nombreLocal: String,
    var horaInicio: String,
    var horaCierre: String,
    var miembrosCategorias: HashMap<String, Boolean>,
    var miembrosMenu: HashMap<String, Boolean>
) {
    public constructor() : this(
        "def", "def", "def", HashMap<String, Boolean>(),
        HashMap<String, Boolean>()
    )
}