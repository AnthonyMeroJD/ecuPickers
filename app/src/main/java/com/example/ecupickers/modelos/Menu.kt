package com.example.ecupickers.modelos

data class Menu constructor(
    var nombre: String,
    var miembrosAlimentos: HashMap<String, HashMap<String, String>>
) {

    public constructor() : this("def", HashMap<String,HashMap<String, String> >())
}