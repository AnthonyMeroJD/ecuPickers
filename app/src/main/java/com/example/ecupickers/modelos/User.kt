package com.example.ecupickers.modelos

data class User constructor(
    var nombres: HashMap<String, String>,
    var celular: String,
    var ciudad: String,
    var provincia: String,
    var pais: String,
    var correo: String,
    var clave: String,
    var tipoUser:String,
    var id:String
) {
    public constructor() : this(
        HashMap<String, String>(),
        "def",
        "def",
        "def",
        "def",
        "def",
        "def",
        "def",
        "def"
    )
}