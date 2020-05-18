package com.example.ecupickers.constantes

enum class EnumTipoLocal {
    RESTAURANTE{
        override fun getTipoLocal(): String ="restaurante"

    },BOTIQUE {
        override fun getTipoLocal(): String ="botique"
    };
    abstract fun getTipoLocal():String
}
