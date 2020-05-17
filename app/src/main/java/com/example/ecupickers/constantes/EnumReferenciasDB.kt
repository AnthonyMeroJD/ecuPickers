package com.example.ecupickers.constantes

enum class EnumReferenciasDB {
    PRODUCTO{
        override fun rutaDB(): String ="Producto"
    },MIEMBROSALIMENTOS{
        override fun rutaDB(): String="MiembrosAlimentos"
    },CATEGORIA{
        override fun rutaDB(): String="Categorias"
    };
    abstract fun rutaDB():String
}