package com.example.ecupickers.clases

import com.example.ecupickers.interfaces.ILocal
import com.example.ecupickers.interfaces.IProducto
import com.google.firebase.database.Query


class Insumo(
    override val nombre: String,
    override val precio: String,
    override val descripcion: String,
    override val calificacion: String,
    override val img: String,
    override val miembroMenu: HashMap<String, Boolean>
) :IProducto {


    override fun registrarProducto(IProducto: IProducto): Boolean {
        TODO("Not yet implemented")
    }

    override fun traerProducto(ILocal: ILocal): Query {
        TODO("Not yet implemented")
    }

    override fun traerProductoXTipo(): Query {
        TODO("Not yet implemented")
    }

}