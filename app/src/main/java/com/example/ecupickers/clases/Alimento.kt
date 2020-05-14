package com.example.ecupickers.clases

import com.example.ecupickers.interfaces.ILocal
import com.example.ecupickers.interfaces.IProducto
import com.google.firebase.database.Query

class Alimento: IProducto {
    var tiempoEstimado:String
        get() {
            tiempoEstimado?.let {
                return tiempoEstimado
            }
        }
        set(value) {
            tiempoEstimado=value
        }
    var miembrosCategorias:HashMap<String,Boolean>
        get() {
            miembrosCategorias?.let {
                return miembrosCategorias
            }
        }
        set(value) {miembrosCategorias=value}
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
    override val miembroMenu: HashMap<String, Boolean>
        get() = TODO("Not yet implemented")

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