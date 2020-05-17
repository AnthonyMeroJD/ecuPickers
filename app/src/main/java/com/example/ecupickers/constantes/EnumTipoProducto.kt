package com.example.ecupickers.constantes

enum class EnumTipoProducto {
    ALIMENTO {
        override fun tipoProducto(): String = "alimento"
    },
    INSUMO {
        override fun tipoProducto(): String = "insumo"
    },PRODUCTO{
        override fun tipoProducto(): String = "producto"
    };

    abstract fun tipoProducto(): String
}