package com.example.ecupickers.modelos

import com.example.ecupickers.constantes.EnumTipoLocal

data class Local constructor(
    var nombre: String,
    var miembrosCategorias: HashMap<String, Boolean>,
    var miembrosMenus: HashMap<String, Boolean>,
    var horaIncio: String,
    var horaCierre: String,
    var tipoLocal: String,
    var direccion:String
) {
    public constructor() : this(
        "def",
        HashMap<String, Boolean>(),
        HashMap<String, Boolean>(),
        "def",
        "def",
        "def",
        "def"
    )

}