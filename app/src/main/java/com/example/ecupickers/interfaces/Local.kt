package com.example.ecupickers.interfaces

import android.app.DownloadManager
import com.example.ecupickers.clases.Vendedor

interface Local {
    val nombre:String
    val servicio:String
    val dueño:String
    val ciudad:String
    val calificacion:String
    val direccion:String
    fun registrarLocal(local: Local):Boolean
    fun traerLocal(vendedor: Vendedor):ArrayList<Local>

}