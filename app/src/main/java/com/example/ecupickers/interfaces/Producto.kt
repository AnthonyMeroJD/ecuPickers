package com.example.ecupickers.interfaces

import com.example.ecupickers.constantes.EnumTipoComida
import com.google.firebase.database.Query

interface Producto {
    val nombre:String
    val precio:String
    val descripcion:String
    val calificacion:String
    val img:String
    val local:String
    fun registrarProducto(producto: Producto):Boolean
    fun traerProducto(local: Local):Query
    fun traerProductoXTipo():Query



}