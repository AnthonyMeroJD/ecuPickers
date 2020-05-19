package com.example.ecupickers.modelos

data class Alimento constructor(
    var nombre: String,
    var precio: String,
    var tiempoEstimado: String,
    var descripcion: String,
    var calificacion:String,
    var miembrosMenus: HashMap<String, Boolean>,
    var miembrosCategorias: HashMap<String, Boolean>
) {
    public constructor() : this(
        "def", "def","def", "def", "def", HashMap<String, Boolean>()
        ,HashMap<String,Boolean>()
    )
}