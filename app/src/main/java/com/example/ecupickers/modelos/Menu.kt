package com.example.ecupickers.modelos

data class Menu constructor(
    var nombre: String,
    var miembrosAlimentos: HashMap<String, HashMap<String, String>>,
    var id: String
) {


    public constructor() : this("def", HashMap<String, HashMap<String, String>>(),"def")
}