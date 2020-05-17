package com.example.ecupickers.modelos

data class User constructor(
    var nombres: HashMap<String, String>,
    var celular: String,
    var Ciudad: String,
    var Provincia: String,
    var Pais: String,
    var correo: String,
    var clave: String,
    var tipoUser:String
) {
    public constructor() : this(
        HashMap<String, String>(),
        "def",
        "def",
        "def",
        "def",
        "def",
        "def",
        "def"
    )
}