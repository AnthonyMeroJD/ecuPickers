package com.example.ecupickers.interfaces

import com.example.ecupickers.clases.Vendedor

interface ILocal {
    val nombre:String
    val miembroCategoria:HashMap<String,Boolean>
    val horaIncio:String
    val horaFinal:String
    fun registrarLocal(ILocal: ILocal):Boolean
    fun traerLocal(vendedor: Vendedor):ArrayList<ILocal>

}