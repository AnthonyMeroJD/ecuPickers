package com.example.ecupickers.clases

import com.example.ecupickers.constantes.*
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Local
import com.example.ecupickers.modelos.Restaurante
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

/*
*----------------------GESTION DE CREACION DE NUEVOS DATOS Y EDICION EN LA BASE DE DATOS-----------
* Esta clase gestiona los nodos
* LOCALES COMPLETAMENTE
* ,MIEMBROSRESTAURANTES COMPLETAMENTE
* ,MIEMBROSBOTIQUE COMPLETAMENTE
* ,CATEGORIASRESTAURANTES COMPLETAMENTE
* Creacion de nuevos locales |--fun crearLocal--|
* asi como tambien la edicion de campos en los nodos LOCALES,MIEMBROSRESTAURANTES |--fun gestionarCampo--|
* En el nodo LOCALES la indexion de menus al sub nodo miembrosMenu  |--fun gestionarMenu--|
* En los nodos LOCALES,MIEMBROSRESTAURANTES
* la anidacion de  categorias al subnodo miembroCategorias |--fun gestionarCategoriaLocal--|
*En EL NODO CATEGORIASRESTAURANTES
* la indexacion de un local a su catalogo de categorias |--fun gestionarCategoriaLocal--|

* */
class Locales {
    /*
   * Gestiona la edicion de un campo especifico en los dos nodos Locales y restaurantes
   * ojo esta funcion sirve con los campos sencillos no anidados
   * ademas con este puedo cambiar el estado del el local
   * */
    fun gestionarCampo(
        valor: String,
        idLocal: String,
        tipoLocal: EnumTipoLocal,
        ciudad: String,
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
                        "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad}/" +
                                "${idLocal}/${campo.getCampos()}", valor
                    )
                }


            }
            EnumTipoLocal.BOTIQUE.getTipoLocal() -> {
                childUpdates.put(
                    "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/" +
                            "${campo.getCampos()}", valor
                )
            }
        }

        ref.updateChildren(childUpdates)
    }

    /*
    * Gestiona la anidacion/eliminar de nuevos campos en miembros Categorias
    * dentro del nodos Locales y agrega a categoriaRestaurantes a sus categorias
    * */
    fun gestionarCategoriaLocal(
        eliminar: Boolean = false,
        categorias: ArrayList<EnumCategoria>,
        idLocal: String,
        ciudad: String
    ) {
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        fun referenciaCategoria(
            categorias: ArrayList<EnumCategoria>,
            tf: Boolean
        ): HashMap<String, Any> {
            val childUpdates = HashMap<String, Any>()

            for (categoria in categorias) {
                childUpdates.put(
                    "${EnumReferenciasDB.CATEGORIASRESTAURANTE.rutaDB()}/${ciudad}/" +
                            "${categoria.getCategoria()}/${idLocal}", tf
                )
                childUpdates.put(
                    "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/${EnumCamposDB.MIEMBROSCATEGORIAS.getCampos()}/" +
                            "${categoria.getCategoria()}", tf
                )
                childUpdates.put(
                    "${EnumReferenciasDB.MIEMBROSRESTAURANTES.rutaDB()}/${ciudad}" +
                            "/${idLocal}/${EnumCamposDB.MIEMBROSCATEGORIAS.getCampos()}/" +
                            "${categoria.getCategoria()}", tf
                )
            }
            return childUpdates
        }
        if (eliminar) {
            var childUpdates = referenciaCategoria(categorias, false)
            ref.updateChildren(childUpdates)
        } else {
            var childUpdates = referenciaCategoria(categorias, true)
            ref.updateChildren(childUpdates)
        }
    }

    /*
    * Gestiona la anidacion de nuevos menus en el nodo Local
    * */
    fun gestionarMenu(
        idMenu: String,
        nombreMenu: String,
        idLocal: String,
        eliminar: Boolean = false
    ) {
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        val childUpdates = HashMap<String, Any>()
        //aqui elimina
        if (eliminar) {
            childUpdates.put(
                "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/" +
                        "${EnumCamposDB.MIEMBROSMENUS.getCampos()}/" +
                        "${idMenu}/${EnumCamposDB.NOMBRE.getCampos()}",
                "false"
            )

        } else {
            childUpdates.put(
                "${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/" +
                        "${EnumCamposDB.MIEMBROSMENUS.getCampos()}/" +
                        "${idMenu}/${EnumCamposDB.NOMBRE.getCampos()}",
                nombreMenu
            )
        }
        ref.updateChildren(childUpdates)
    }

    /*
    * gestiona la creacion del local segun el tipo requerido y retorna la key del local
    * */
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
                    local.estado,
                    local.horaIncio,
                    local.horaCierre,
                    local.miembrosCategorias
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