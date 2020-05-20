package com.example.ecupickers.modelos

import com.example.ecupickers.constantes.EnumTipoLocal

data class Local constructor(
    var nombre: String,
    var estado:String,
    var miembrosMenus: HashMap<String, HashMap<String, String>>,
    var miembrosCategorias: HashMap<String, Boolean>,
    var horaIncio: String,
    var horaCierre: String,
    var tipoLocal: String,
    var direccion: String

) {
    public constructor() : this(
        "def",
        "def",
        HashMap<String, HashMap<String, String>>(),
        HashMap<String, Boolean>(),
        "def",
        "def",
        "def",
        "def"

    )

}