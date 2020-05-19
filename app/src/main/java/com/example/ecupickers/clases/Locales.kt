package com.example.ecupickers.clases

import com.example.ecupickers.constantes.*
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Local
import com.example.ecupickers.modelos.Restaurante

class Locales {
        /*
       * Gestiona la edicion de un campo especifico en los dos nodos Locales y restaurantes
       * ojo esta funcion sirve con los campos sencillos no anidados
       * */
    fun gestionarCampo(
        valor: String,
        idLocal: String,
        tipoLocal: EnumTipoLocal,
        ciudad: EnumCiudad,
        campo: EnumCamposDB
    ) {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        when (tipoLocal.getTipoLocal()) {
            EnumTipoLocal.RESTAURANTE.getTipoLocal() -> {
                //si el valor del campo es el tipop del local solo se actualiza el nodo Locales
                //ya que no cuenta el nodo restaurante con un campo de ese tipo
                if (campo.getCampos().equals(EnumCamposDB.TIPOLOCAL.getCampos())) {
                    childUpdates.put(
                        "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/" +
                                "${campo.getCampos()}", valor
                    )
                } else {
                    childUpdates.put(
                        "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/" +
                                "${campo.getCampos()}", valor
                    )
                    childUpdates.put(
                        "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}/" +
                                "${idLocal}/${campo.getCampos()}", valor
                    )
                }
                ref.updateChildren(childUpdates)
            }
            EnumTipoLocal.BOTIQUE.getTipoLocal() -> {
                childUpdates.put(
                    "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/" +
                            "${campo.getCampos()}", valor
                )
                ref.updateChildren(childUpdates)
            }
        }
    }
    /*
    * Gestiona la anidacion/eliminar de nuevos campos en miembros Menus y miembros Categorias
    * dentro de los nodos Locales y Restaurante
    * */
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

            for (categoria in categorias) {
                childUpdates.put(
                    "${EnumReferenciasDB.CATEGORIASRESTAURANTE.rutaDB()}/${ciudad.getCiudad()}/" +
                            "${categoria.getCategoria()}/${idLocal}", tf
                )
                childUpdates.put(
                    "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/${EnumCamposDB.MIEMBROSCATEGORIAS.getCampos()}/" +
                            "${categoria.getCategoria()}", tf
                )
                childUpdates.put(
                    "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}" +
                            "/${idLocal}/${EnumCamposDB.MIEMBROSCATEGORIAS.getCampos()}/${categoria.getCategoria()}",
                    tf
                )
            }
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
        childUpdates.put(
            "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/${EnumCamposDB.HORAINICIO.getCampos()}",
            horaInicio
        )
        childUpdates.put(
            "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/${EnumCamposDB.HORACIERRE.getCampos()}",
            horaCierre
        )
        childUpdates.put(
            "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}/${idLocal}/${EnumCamposDB.HORAINICIO.getCampos()}",
            horaInicio
        )
        childUpdates.put(
            "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}/${idLocal}/${EnumCamposDB.HORACIERRE.getCampos()}",
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
                "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/${EnumCamposDB.MIEMBROSMENUS.getCampos()}/${idMenu}",
                true
            )
            childUpdates.put(
                "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}/${idLocal}/${EnumCamposDB.MIEMBROSMENUS.getCampos()}/${idMenu}",
                true
            )
        } else {
            childUpdates.put(
                "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/${EnumCamposDB.MIEMBROSMENUS.getCampos()}/${idMenu}",
                false
            )
            childUpdates.put(
                "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}/${idLocal}/${EnumCamposDB.MIEMBROSMENUS.getCampos()}/${idMenu}",
                false
            )
        }
        ref.updateChildren(childUpdates)
    }

    fun crearLocal(
        local: Local,
        ciudad: EnumCiudad

    ): String {
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        val childUpdates = HashMap<String, Any>()
        val key = ref.push().key

        when (local.tipoLocal) {
            EnumTipoLocal.RESTAURANTE.getTipoLocal() -> {
                var restaurante = Restaurante(
                    local.nombre,
                    local.horaIncio,
                    local.horaCierre,
                    local.miembrosCategorias,
                    local.miembrosMenus
                )
                childUpdates.put("${EnumReferenciasDB.LOCALES.rutaDB()}/${key}", local)
                childUpdates.put(
                    "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad.getCiudad()}/${key}",
                    restaurante
                )
                ref.updateChildren(childUpdates)

            }
            EnumTipoLocal.BOTIQUE.getTipoLocal() -> {
                childUpdates.put("${EnumReferenciasDB.LOCALES.rutaDB()}/${key}", local)
                childUpdates.put(
                    "${EnumReferenciasDB.MIEMBROSBOTIQUE.rutaDB()}/${ciudad.getCiudad()}/${key}",
                    true
                )
                ref.updateChildren(childUpdates)
            }
        }
        return key.toString()
    }
}