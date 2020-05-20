package com.example.ecupickers.constantes

enum class EnumCamposDB {
    ATENDIDOS {
        override fun getCampos(): String = "atendidos"
    },
    NOMBRE {
        override fun getCampos(): String = "nombre"
    },
    PRECIO {
        override fun getCampos(): String = "precio"
    },
    TIEMPOESTIMADO {
        override fun getCampos(): String = "tiempoEstimado"
    },
    DESCRIPCION {
        override fun getCampos(): String = "descripcion"
    },
    CALIFICACION {
        override fun getCampos(): String = "calificacion"
    },
    MIEMBROSMENUS {
        override fun getCampos(): String = "miembrosMenus"
    },
    MIEMBROSCATEGORIAS {
        override fun getCampos(): String = "miembrosCategorias"
    },
    TRANSPORTE {
        override fun getCampos(): String = "transporte"
    },
    HORAINICIO {
        override fun getCampos(): String = "horaInicio"
    },
    HORACIERRE {
        override fun getCampos(): String = "horaCierre"
    },
    TIPOLOCAL {
        override fun getCampos(): String = "tipoLocal"
    },
    MIEMBROSALIMENTOS {
        override fun getCampos(): String = "miembrosAlimentos"
    },
    LOCAL {
        override fun getCampos(): String = "local"
    },
    CLIENTE {
        override fun getCampos(): String = "cliente"
    },
    ESTADO {
        override fun getCampos(): String = "estado"
    },
    MIEMBROSPRODUCTOS {
        override fun getCampos(): String = "miembrosProductos"
    },
    IMAGEN {
        override fun getCampos(): String = "imagen"
    },
    NOMBRES {
        override fun getCampos(): String = "nombres"
    },
    PRIMERNOMBRE {
        override fun getCampos(): String = "primerNombres"
    },
    SEGUNDONOMBRE {
        override fun getCampos(): String = "segundoNombres"
    },
    PRIMERAPELLIDO {
        override fun getCampos(): String = "primerApellido"
    },
    SEGUNDOAPELLIDO {
        override fun getCampos(): String = "segundoApellido"
    },
    CELULAR {
        override fun getCampos(): String = "celular"
    },
    CIUDAD {
        override fun getCampos(): String = "ciudad"
    },
    PROVINCIA {
        override fun getCampos(): String = "provincia"
    },
    PAIS {
        override fun getCampos(): String = "pais"
    },
    CORREO {
        override fun getCampos(): String = "correo"
    },
    CLAVE {
        override fun getCampos(): String = "clave"
    },
    TIPOUSER {
        override fun getCampos(): String = "tipoUser"
    },
    LOCALES {
        override fun getCampos(): String = "locales"
    },
    FECHA {
        override fun getCampos(): String ="fecha"
    },
    HORAPEDIDO {
        override fun getCampos(): String ="horaPedido"
    },
    HORAENTREGA {
        override fun getCampos(): String ="horaEntrega"
    },
    DIRECCIONENTREGA{
        override fun getCampos(): String ="direccionEntrega"
    }
    ;


    abstract fun getCampos(): String
}