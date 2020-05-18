package com.example.ecupickers.modelos

import com.example.ecupickers.constantes.EnumTipoLocal

data class Local constructor(
    var nombre: String,
    var miembroCategoria: HashMap<String, Boolean>,
    var miembrosMenu: HashMap<String, Boolean>,
    var horaIncio: String,
    var horaFinal: String,
    var tipoLocal: String
) {
    public constructor() : this(
        "def",
        HashMap<String, Boolean>(),
        HashMap<String, Boolean>(),
        "def",
        "def",
        "def"
    )

}