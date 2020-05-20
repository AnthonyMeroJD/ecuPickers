package com.example.ecupickers.constantes

enum class EnumEstado {
    Activo{
        override fun getEstado(): String ="activo"
    };
    abstract fun getEstado():String

}