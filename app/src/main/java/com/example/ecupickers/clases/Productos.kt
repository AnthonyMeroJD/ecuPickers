package com.example.ecupickers.clases

import com.example.ecupickers.constantes.EnumCamposDB
import com.example.ecupickers.constantes.EnumCategoria
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.constantes.EnumTipoProducto
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Alimento
import com.example.ecupickers.modelos.Producto

class Productos {
    fun crearProducto(producto: Producto): String {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        val key = ref.push().key
        childUpdates.put("${EnumReferenciasDB.PRODUCTOS.rutaDB()}/${key}", producto)
        return key.toString()
    }

    fun crearAlimento(
        idProducto: String,
        producto: Producto,
        tiempoEstimado: String,
        miembroMenu: HashMap<String, Boolean>,
        miembroCategoria: HashMap<String, Boolean>,
        idLocal: String
    ): String {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        val alimento = Alimento(
            producto.nombre, producto.precio, tiempoEstimado, producto.descripcion,
            producto.calificacion,miembroMenu,miembroCategoria
        )
        childUpdates.put(
            "${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/${idLocal}/${idProducto}",
            alimento
        )
        ref.updateChildren(childUpdates)
        return idProducto
    }

    fun crearInsumo(
        idProducto: String,
        idLocal: String
    ): String {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)

        childUpdates.put(
            "${EnumReferenciasDB.MIEMBROINSUMO.rutaDB()}/${idLocal}/${idProducto}",
            true
        )
        ref.updateChildren(childUpdates)
        return idProducto
    }
    fun añadirProductoAMenu(idMenu:String,idLocal: String,idAlimento: ArrayList<String>){
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        for (alimento in idAlimento){
            childUpdates.put("${EnumReferenciasDB.MENUS.rutaDB()}/${idMenu}/" +
                    "${EnumCamposDB.MIEMBROSALIMENTOS}/${alimento}",true)
        }

        childUpdates.put("${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/${idLocal}" +
                "/${idMenu}/${EnumCamposDB.MIEMBROSMENUS}/${idMenu}",true)
        ref.updateChildren(childUpdates)
    }
}