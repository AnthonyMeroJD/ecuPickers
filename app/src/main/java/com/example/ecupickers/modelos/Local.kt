package com.example.ecupickers.modelos

import com.example.ecupickers.constantes.EnumTipoLocal

data class Local constructor(
    var nombre: String,
    var estado: String,
    var miembrosMenus: HashMap<String, HashMap<String, String>>,
    var miembroscategorias: HashMap<String, Boolean>,
    var horaInicio: String,
    var horaCierre: String,
    var tipoLocal: String,
    var direccion: String,
    var id: String

) {


    public constructor() : this(
        "def",
        "def",
        HashMap<String, HashMap<String, String>>(),
        HashMap<String, Boolean>(),
        "def",
        "def",
        "def",
        "def",
        "def"
    )

}