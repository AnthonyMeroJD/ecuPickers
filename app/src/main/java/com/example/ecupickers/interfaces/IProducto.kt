package com.example.ecupickers.interfaces

import com.google.firebase.database.Query

interface IProducto {
    val nombre:String
    val precio:String
    val descripcion:String
    val calificacion:String
    val img:String
   val miembroMenu:HashMap<String,Boolean>
    fun registrarProducto(IProducto: IProducto):Boolean
    fun traerProducto(ILocal: ILocal):Query
    fun traerProductoXTipo():Query



}