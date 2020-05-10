package com.example.ecupickers.clases

import com.example.ecupickers.constantes.EnumTipoInsumo
import com.example.ecupickers.interfaces.Local
import com.example.ecupickers.interfaces.Producto
import com.google.firebase.database.Query


class Insumo:Producto {
    constructor( tipoInsumo: String,unitario:Boolean,cantidad:String)
    override val nombre: String
        get() = TODO("Not yet implemented")
    override val precio: String
        get() = TODO("Not yet implemented")
    override val descripcion: String
        get() = TODO("Not yet implemented")
    override val calificacion: String
        get() = TODO("Not yet implemented")
    override val img: String
        get() = TODO("Not yet implemented")
    override val local: String
        get() = TODO("Not yet implemented")

    override fun registrarProducto(producto: Producto): Boolean {
        TODO("Not yet implemented")
    }

    override fun traerProducto(local: Local): Query {
        TODO("Not yet implemented")
    }

    override fun traerProductoXTipo(): Query {
        TODO("Not yet implemented")
    }
}