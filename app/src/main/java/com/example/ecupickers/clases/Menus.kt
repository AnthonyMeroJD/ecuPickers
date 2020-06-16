package com.example.ecupickers.clases

import com.example.ecupickers.modelos.Menu
import com.example.ecupickers.constantes.EnumCamposDB
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.factory.DbReference

/*----------------------GESTION DE CREACION DE NUEVOS DATOS Y EDICION EN LA BASE DE DATOS-----------
* Esta clase gestiona los nodos
*  MENUS COMPLETAMENTE ,
* En el nodo MENUS controla toda la edicion de campos,|--fun gestionarNombre--|
* agregar un Producto al subnodo miembrosAlimentos,|--fun gestionarAlimentosAmenu--|
* crear un nuevo menu,
* En el nodo MIEMBROSALIMENTOS  gestiona la indexaxion de un menu al subnodo miembrosMenus
* */
class Menus {


    fun gestionarNombre(nombre: String, idLocal: String, idMenu: String) {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        //ojo si la ruta no existe la crea e inserta los datos
        childUpdates["${EnumReferenciasDB.LOCALES.rutaDB()}/${idLocal}/" +
                "${EnumCamposDB.MIEMBROSMENUS.getCampos()}/${idMenu}/" +
                "${EnumCamposDB.NOMBRE.getCampos()}"] =
            nombre
        ref.updateChildren(childUpdates)
    }

    fun crearMenu(menu: Menu): String {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        val key = ref.push().key
        childUpdates.put("${EnumReferenciasDB.MENUS.rutaDB()}/${key}", menu)
        ref.updateChildren(childUpdates)
        return key.toString()
    }

    fun gestionarAlimentosAMenu(
        idAlimento: String,
        nombreAlimento: String,
        descripcion: String,
        precio: String,
        idMenu: String
    ) {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)

        childUpdates.put(
            "${EnumReferenciasDB.MENUS.rutaDB()}/${idMenu}/" +
                    "${EnumCamposDB.MIEMBROSALIMENTOS.getCampos()}/" +
                    "${idAlimento}/${EnumCamposDB.NOMBRE.getCampos()}", nombreAlimento
        )
        childUpdates.put(
            "${EnumReferenciasDB.MENUS.rutaDB()}/${idMenu}/" +
                    "${EnumCamposDB.MIEMBROSALIMENTOS.getCampos()}/" +
                    "${idAlimento}/${EnumCamposDB.DESCRIPCION.getCampos()}", descripcion
        )
        childUpdates.put(
            "${EnumReferenciasDB.MENUS.rutaDB()}/${idMenu}/" +
                    "${EnumCamposDB.MIEMBROSALIMENTOS.getCampos()}/" +
                    "${idAlimento}/${EnumCamposDB.PRECIO.getCampos()}", precio
        )

        ref.updateChildren(childUpdates)

    }

    fun eliminarMenu(idLocal: String, idMenu: String) {
        val childUpdates = HashMap<String, Any?>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        childUpdates["${EnumReferenciasDB.MENUS.rutaDB()}/$idMenu"] = null
        childUpdates["${EnumReferenciasDB.LOCALES.rutaDB()}/$idLocal/" +
                "${EnumCamposDB.MIEMBROSMENUS.getCampos()}/${idMenu}"]=null
        ref.updateChildren(childUpdates)
    }
}