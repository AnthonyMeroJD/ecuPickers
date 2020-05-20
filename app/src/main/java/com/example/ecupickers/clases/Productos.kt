package com.example.ecupickers.clases

import com.example.ecupickers.constantes.*
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Alimento
import com.example.ecupickers.modelos.Producto

/*---------------------GESTION DE CREACION DE NUEVOS DATOS Y EDICION EN LA BASE DE DATOS-----------
* Esta clase gestiona los nodos
* PRODUCTOS
* MIEMBROSALIMENTOS
* MIEMBROSINSUMOS
*Crear producto en el nodo PRODUCTOS |--crearProducto--|
* Crear Alimento en el nodo MIEMBROSALIMENTOS |--crear alimento--| ojo se debe tener en cuenta
* que se debe pasar el mismo id quue retorna la funcion crear producto en el parametro de esta funcion
* Crear insumo en el nodo MIEMBROSALIEMTENTOS |--crear insumo--| ojo se debe tener en cuenta lo
* mismo que en la anterior funcion
* Añadir un menu al Producto en el nodo MIEMBROS ALIMENTOS,ademas añade el producto en el nodo MENU,
* al subnodo MIEMBROSALIMENTOS|--añadirProductoAMenu--|
* AÑADIR O ELIMINAR una categoria al producto en el nodo MIEMBROALIMENTOS |--añadirCategoriasAAliemto--|
* Editar campos sencillos en los nodos PRODUCTOS Y MIEMBROSALIMENTOS
*/
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
            producto.nombre, producto.precio, tiempoEstimado
            , producto.descripcion, "0", "0", miembroMenu, miembroCategoria
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

    fun añadirProductoAMenu(idMenu: String, idLocal: String, idAlimento: ArrayList<String>) {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        for (alimento in idAlimento) {
            childUpdates.put(
                "${EnumReferenciasDB.MENUS.rutaDB()}/${idMenu}/" +
                        "${EnumCamposDB.MIEMBROSALIMENTOS}/${alimento}", true
            )
        }

        childUpdates.put(
            "${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/${idLocal}" +
                    "/${idMenu}/${EnumCamposDB.MIEMBROSMENUS}/${idMenu}", true
        )
        ref.updateChildren(childUpdates)
    }

    fun gestionarCategoriasAAlimento(
        idAlimento: String,
        categorias: ArrayList<EnumCategoria>,
        idLocal: String,
        eliminar:Boolean=false
    ) {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        for (categoria in categorias) {
            childUpdates.put(
                "${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/" +
                        "${idLocal}/${idAlimento}/" +
                        "${EnumCamposDB.MIEMBROSCATEGORIAS.getCampos()}/${categoria}",
                !eliminar
            )
        }
        ref.updateChildren(childUpdates)
    }

    fun gestionarCampo(
        valor: String,
        idLocal: String,
        idProducto: String,
        campo: EnumCamposDB
    ) {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        when(campo.getCampos()){
            EnumCamposDB.TIEMPOESTIMADO.getCampos()->{
                childUpdates.put("${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/" +
                        "${idLocal}/${idProducto}/${campo.getCampos()}",valor)
            }
            else->{
                childUpdates.put("${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/" +
                        "${idLocal}/${idProducto}/${campo.getCampos()}",valor)
                childUpdates.put("${EnumReferenciasDB.PRODUCTOS.rutaDB()}/" +
                        "${idProducto}/${campo.getCampos()}",valor)
            }
        }
    }
}