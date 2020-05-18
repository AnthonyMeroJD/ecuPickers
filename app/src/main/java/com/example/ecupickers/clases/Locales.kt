package com.example.ecupickers.clases

import com.example.ecupickers.constantes.EnumCategoria
import com.example.ecupickers.constantes.EnumCiudad
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.constantes.EnumTipoLocal
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Local
import com.example.ecupickers.modelos.Restaurante

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
        eliminar: Boolean = false,
        categorias: ArrayList<EnumCategoria>,
        idLocal: String,
        ciudad: EnumCiudad
    ) {
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)

        fun referenciaCategoria(
            categorias: ArrayList<EnumCategoria>,
            tf: Boolean
        ): HashMap<String, Any> {
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
            return childUpdates
        }
        if (!eliminar) {
            var childUpdates = referenciaCategoria(categorias, true)
            ref.updateChildren(childUpdates)
        } else {
            var childUpdates = referenciaCategoria(categorias, false)
            ref.updateChildren(childUpdates)
        }
    }

    fun gestionarHorario(
        horaInicio: String,
        horaCierre: String,
        idLocal: String,
        ciudad: EnumCiudad
    ) {
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        val childUpdates = HashMap<String, Any>()
        childUpdates.put("${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/horaInicio", horaInicio)
        childUpdates.put("${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/horaCierre", horaCierre)
        childUpdates.put(
            "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}/${idLocal}/horaInicio",
            horaInicio
        )
        childUpdates.put(
            "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}/${idLocal}/horaCierre",
            horaCierre
        )
        ref.updateChildren(childUpdates)
    }

    fun gestionarMenu(
        idMenu: String,
        ciudad: EnumCiudad,
        idLocal: String,
        eliminar: Boolean = false
    ) {
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        val childUpdates = HashMap<String, Any>()
        if (!eliminar) {
            childUpdates.put(
                "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/miembrosMenu/${idMenu}",
                true
            )
            childUpdates.put(
                "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}/${idLocal}/miembrosMenu/${idMenu}",
                true
            )
        } else {
            childUpdates.put(
                "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/miembrosMenu/${idMenu}",
                false
            )
            childUpdates.put(
                "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}/${idLocal}/miembrosMenu/${idMenu}",
                true
            )
        }
        ref.updateChildren(childUpdates)
    }

    fun gestionarLocal(
        local: Local,
        ciudad: EnumCiudad,
        idLocal: String?,
        eliminar: Boolean = false
    ): String {
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        val childUpdates = HashMap<String, Any>()
        val key=ref.push().key
        fun eliminarLocal(idLocal: String) {}
        when (local.tipoLocal) {
            EnumTipoLocal.RESTAURANTE.getTipoLocal() -> {
                if (!eliminar && idLocal!!.isNotEmpty()) {
                    eliminarLocal(idLocal!!)
                } else {
                    var restaurante = Restaurante(
                        local.nombre,
                        local.horaIncio,
                        local.horaFinal,
                        local.miembroCategoria,
                        local.miembrosMenu
                    )

                    childUpdates.put("${EnumReferenciasDB.LOCALES.rutaDB()}/${key}",local)
                    childUpdates.put("${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}/${key}",restaurante)
                    ref.updateChildren(childUpdates)

                }

            }
            EnumTipoLocal.BOTIQUE.getTipoLocal() -> {
            }
        }
        return key.toString()
    }
}