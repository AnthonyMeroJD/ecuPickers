package com.example.ecupickers.modelos

data class MiembrosAlimentos(var miembrosAlimentos:HashMap<String,HashMap<String,String>>) {
    public constructor():this(HashMap<String,HashMap<String,String>>())
}