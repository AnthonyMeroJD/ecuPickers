package com.example.ecupickers.constantes

enum class EnumCategoria {
    ALMUERZO{
        override fun getCategoria():String="Almuerzo"

    };
    abstract fun getCategoria():String


}
