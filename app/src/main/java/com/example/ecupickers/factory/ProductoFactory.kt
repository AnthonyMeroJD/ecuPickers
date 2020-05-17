package com.example.ecupickers.factory

import com.example.ecupickers.clases.Alimento
import com.example.ecupickers.clases.Botique
import com.example.ecupickers.clases.Insumo
import com.example.ecupickers.clases.Producto
import com.example.ecupickers.constantes.EnumTipoProducto
import com.example.ecupickers.interfaces.IProducto

object ProductoFactory {
    fun getProducto(
        tipoProducto: EnumTipoProducto,
        nombre: String,
        precio: String,
        descripcion: String,
        calificacion: String,
        img: String,
        miembroMenu: HashMap<String, Boolean>
    ): IProducto {
        return when (tipoProducto.tipoProducto()) {
            EnumTipoProducto.ALIMENTO.tipoProducto() -> Alimento(
                nombre,
                precio,
                descripcion,
                calificacion,
                img,
                miembroMenu
            )
            EnumTipoProducto.INSUMO.tipoProducto() -> Insumo(
                nombre,
                precio,
                descripcion,
                calificacion,
                img,
                miembroMenu
            )
            EnumTipoProducto.INSUMO.tipoProducto()->Producto(
                nombre,
                precio,
                descripcion,
                calificacion,
                img,
                miembroMenu
            )
            /*nunca se ejecutara esta linea pero no me la deja quitar por el uso del when*/
            else ->  Alimento(
                nombre,
                precio,
                descripcion,
                calificacion,
                img,
                miembroMenu
            )
        }

    }
}