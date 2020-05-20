package com.example.ecupickers.clases

import android.view.Menu
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
    fun gestionarNombre(nombre: String, idMenu: String) {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        //ojo si la ruta no existe la crea e inserta los datos
        childUpdates.put("${EnumReferenciasDB.MENUS.rutaDB()}/${idMenu}/${EnumCamposDB.NOMBRE.getCampos()}"
            , nombre)
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

    fun gestionarAlimentosAMenu(idAlimento: ArrayList<String>, idMenu: String,eliminar:Boolean=false)
    {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        for (alimento in idAlimento) {
            childUpdates.put("${EnumReferenciasDB.MENUS.rutaDB()}/${idMenu}/${EnumCamposDB.MIEMBROSALIMENTOS.getCampos()}/${alimento}",!eliminar)
        }
        ref.updateChildren(childUpdates)

    }

}