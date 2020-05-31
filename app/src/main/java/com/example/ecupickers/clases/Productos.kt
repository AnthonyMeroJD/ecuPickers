package com.example.ecupickers.clases

import com.example.ecupickers.constantes.*
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.Alimento
import com.example.ecupickers.modelos.Menu
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
        producto.id = key.toString()
        childUpdates.put("${EnumReferenciasDB.PRODUCTOS.rutaDB()}/${key}", producto)
        ref.updateChildren(childUpdates)
        return key.toString()
    }

    fun crearAlimento(
        idProducto: String,
        producto: Producto,
        tiempoEstimado: String,
        miembroMenu: HashMap<String, Boolean>,
        miembroCategoria: HashMap<String, Boolean>,
        idLocal: String
    ): Alimento {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        val alimento = Alimento()
        alimento.apply {
            this.nombre = producto.nombre
            this.precio = producto.precio
            this.tiempoEstimado = tiempoEstimado
            this.descripcion = producto.descripcion
            this.atendidos = "0"
            this.calificacion = "0"
            this.miembrosMenus = miembroMenu
            this.miembroscategorias = miembroCategoria
            this.id = producto.id
        }
        childUpdates.put(
            "${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/${idLocal}/${idProducto}",
            alimento
        )
        ref.updateChildren(childUpdates)
        return alimento
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

    fun añadirProductosAMenu(
        idMenu: String,
        productosId: HashMap<String,HashMap<EnumCamposDB, String>>,
        idLocal: String
    ) {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        for (producto in productosId){
            for (campo in producto.value){
                childUpdates.put("${EnumReferenciasDB.MENUS.rutaDB()}/${idMenu}/" +
                        "${EnumCamposDB.MIEMBROSALIMENTOS.getCampos()}/${producto.key}/" +
                        "${campo.key.getCampos()}",campo.value)
                childUpdates.put("${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/${idLocal}/" +
                        "${producto.key}/${EnumCamposDB.MIEMBROSMENUS.getCampos()}/${idMenu}",true)
            }
        }
        ref.updateChildren(childUpdates)
    }

    fun añadirProductoAMenu(
        idMenus: ArrayList<String>,
        idLocal: String,
        idAlimento: String,
        alimento: Alimento
    ) {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        var nombre = alimento.nombre
        var descripcion = alimento.descripcion
        var precio = alimento.precio

        for (menu in idMenus) {
            childUpdates.put(
                "${EnumReferenciasDB.MENUS.rutaDB()}/${menu}/" +
                        "${EnumCamposDB.MIEMBROSALIMENTOS.getCampos()}" +
                        "/${idAlimento}/${EnumCamposDB.NOMBRE.getCampos()}", nombre
            )
            childUpdates.put(
                "${EnumReferenciasDB.MENUS.rutaDB()}/${menu}/" +
                        "${EnumCamposDB.MIEMBROSALIMENTOS.getCampos()}" +
                        "/${idAlimento}/${EnumCamposDB.DESCRIPCION.getCampos()}", descripcion
            )
            childUpdates.put(
                "${EnumReferenciasDB.MENUS.rutaDB()}/${menu}/" +
                        "${EnumCamposDB.MIEMBROSALIMENTOS.getCampos()}" +
                        "/${idAlimento}/${EnumCamposDB.PRECIO.getCampos()}", precio
            )
            childUpdates.put(
                "${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/${idLocal}" +
                        "/${idAlimento}/${EnumCamposDB.MIEMBROSMENUS.getCampos()}" +
                        "/${menu}", true
            )
        }



        ref.updateChildren(childUpdates)
    }

    fun gestionarCategoriasAAlimento(
        idAlimento: String,
        categorias: ArrayList<EnumCategoria>,
        idLocal: String,
        eliminar: Boolean = false
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
        when (campo.getCampos()) {
            EnumCamposDB.TIEMPOESTIMADO.getCampos() -> {
                childUpdates.put(
                    "${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/" +
                            "${idLocal}/${idProducto}/${campo.getCampos()}", valor
                )
            }
            else -> {
                childUpdates.put(
                    "${EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB()}/" +
                            "${idLocal}/${idProducto}/${campo.getCampos()}", valor
                )
                childUpdates.put(
                    "${EnumReferenciasDB.PRODUCTOS.rutaDB()}/" +
                            "${idProducto}/${campo.getCampos()}", valor
                )
            }
        }
    }
}