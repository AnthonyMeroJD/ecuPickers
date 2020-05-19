package com.example.ecupickers.constantes

enum class EnumReferenciasDB {
    PRODUCTOS {
        override fun rutaDB(): String = "Productos"
    },
    MIEMBROSALIMENTOS {
        override fun rutaDB(): String = "MiembrosAlimentos"
    },
    MIEMBROINSUMO{
        override fun rutaDB(): String ="MiembrosInsumos"
    },
    CATEGORIA {
        override fun rutaDB(): String = "Categorias"
    },
    USERS {
        override fun rutaDB(): String= "Users"
    },
    MIEMBROSDELIVERY {
        override fun rutaDB(): String = "MiembrosDelivery"
    },
    MIEMBROSCONSUMIDORES {
        override fun rutaDB(): String = "MiembrosConsumidores"
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
    MIEMBROSBOTIQUE{
        override fun rutaDB(): String ="miembrosBotique"
    },
    ROOT{
        override fun rutaDB(): String ="root"
    };

    abstract fun rutaDB(): String
}