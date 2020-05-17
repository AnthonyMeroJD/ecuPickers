package com.example.ecupickers.modelos

data class Local constructor(
    var nombre: String,
    var miembroCategoria: HashMap<String, Boolean>,
    var miembrosMenu: HashMap<String, Boolean>,
    var horaIncio: String,
    var horaFinal: String
) {
    public constructor() : this(
        "def",
        HashMap<String, Boolean>(),
        HashMap<String, Boolean>(),
        "def",
        "def"
    )

}