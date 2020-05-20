package com.example.ecupickers.modelos

data class Menu constructor(var nombre: String, var miembrosAlimentos: HashMap<String, Boolean>) {
    public constructor() : this("def", HashMap<String, Boolean>())
}