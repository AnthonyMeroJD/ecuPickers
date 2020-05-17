package com.example.ecupickers.clases

import com.example.ecupickers.constantes.EnumCategoria
import com.example.ecupickers.constantes.EnumCiudad
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.factory.DbReference

class Locales {
    fun gestionarNombre(nombre: String, idLocal: String, ciudad: EnumCiudad) {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        //ojo si la ruta no existe la crea e inserta los datos
        childUpdates.put("${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/nombre", nombre)
        childUpdates.put(
            "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}/${idLocal}/nombreLocal",
            nombre
        )
        ref.updateChildren(childUpdates)
    }

    fun gestionarCategoriaLocal(
        eliminar: Boolean,
        categorias: ArrayList<EnumCategoria>,
        idLocal: String,
        ciudad: EnumCiudad
    ) {
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)

        fun referenciaCategoria(categorias: ArrayList<EnumCategoria>, tf: Boolean):HashMap<String,Any> {
            val childUpdates = HashMap<String, Any>()
            val categoriasTF = HashMap<String, Boolean>()
            for (categoria in categorias) {
                childUpdates.put(
                    "${EnumReferenciasDB.CATEGORIASRESTAURANTE.rutaDB()}/${ciudad.getCiudad()}/" +
                            "${categoria.getCategoria()}/${idLocal}", tf
                )
                categoriasTF.put(categoria.getCategoria(), tf)
            }
            childUpdates.put(
                "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/miembroCategoria",
                categoriasTF
            )
            childUpdates.put(
                "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}/${idLocal}/miembroCategoria",
                categoriasTF
            )
            return  childUpdates
        }
        if (!eliminar) {
            var childUpdates=referenciaCategoria(categorias,true)
            ref.updateChildren(childUpdates)
        } else {
            var childUpdates=referenciaCategoria(categorias,false)
            ref.updateChildren(childUpdates)
        }
    }
    fun gestionarHorario(){
        
    }

}