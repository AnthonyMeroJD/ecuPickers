package com.example.ecupickers.constantes

enum class EnumCategoria {
    ALMUERZO{
        override fun getCategoria():String="Almuerzos"

    },
    DESAYUNOS{
        override fun getCategoria(): String ="Desayunos"
    },
    MERIENDAS{
        override fun getCategoria(): String ="Meriendas"
    },
    POSTRES{
        override fun getCategoria(): String ="Postres"
    },
    BBQ{
        override fun getCategoria(): String ="BBQ"
    },
    COMIDARAPIDA{
        override fun getCategoria(): String ="ComidaRapida"
    },
    MARISCOS{
        override fun getCategoria(): String ="Mariscos"
    },
    POLLOS{
        override fun getCategoria(): String ="Pollos"
    },
    HELADOS{
        override fun getCategoria(): String ="Helados"
    },
    HAMBURGUESAS{
        override fun getCategoria(): String ="Hamburguesas"
    },
    PIZZAS{
        override fun getCategoria(): String ="Pizzas"
    }
    ;
    abstract fun getCategoria():String


}
