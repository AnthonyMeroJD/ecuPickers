package com.example.ecupickers.modelos

data class Restaurante constructor(
    var nombre: String,
    var horaInicio: String,
    var horaCierre: String,
    var miembrosCategorias: HashMap<String, Boolean>,
    var miembrosMenus: HashMap<String, Boolean>
) {
    public constructor() : this(
        "def", "def", "def", HashMap<String, Boolean>(),
        HashMap<String, Boolean>()
    )
}