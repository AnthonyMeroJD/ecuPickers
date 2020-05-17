package com.example.ecupickers.constantes

enum class EnumCiudad {
        QUITO{
            override fun getCiudad(): String="Quito"
        };
    abstract fun getCiudad():String
}