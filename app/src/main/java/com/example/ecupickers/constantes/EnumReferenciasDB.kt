package com.example.ecupickers.constantes

enum class EnumReferenciasDB {
    PRODUCTOS {
        override fun rutaDB(): String = "Productos"
    },
    MIEMBROSALIMENTOS {
        override fun rutaDB(): String = "MiembrosAlimentos"
    },
    CATEGORIA {
        override fun rutaDB(): String = "Categorias"
    },
    USERS {
        override fun rutaDB(): String= "users"
    },
    MIEMBROSDELIVERY {
        override fun rutaDB(): String = "miembrosDelivery"
    },
    MIEMBROSCONSUMIDORES {
        override fun rutaDB(): String = "miembrosConsumidores"
    },
    LOCALES {
        override fun rutaDB(): String = "locales"
    },
    MIEMBROSRESTAURANTES {
        override fun rutaDB(): String = "miembrosRestaurante"
    },
    MENUS {
        override fun rutaDB(): String = "menus"
    },
    PEDIDOS {
        override fun rutaDB(): String="pedidos"
    },
    CATEGORIASRESTAURANTE {
        override fun rutaDB(): String ="categoriasRestaurante"
    },
    ROOT{
        override fun rutaDB(): String ="root"
    };

    abstract fun rutaDB(): String
}