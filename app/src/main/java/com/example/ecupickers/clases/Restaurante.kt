package com.example.ecupickers.clases

import com.example.ecupickers.interfaces.ILocal

class Restaurante:ILocal {
    override val nombre: String
        get() = TODO("Not yet implemented")
    override val miembroCategoria: HashMap<String, Boolean>
        get() = TODO("Not yet implemented")
    override val horaIncio: String
        get() = TODO("Not yet implemented")
    override val horaFinal: String
        get() = TODO("Not yet implemented")

    override fun registrarLocal(ILocal: ILocal): Boolean {
        TODO("Not yet implemented")
    }

    override fun traerLocal(vendedor: Vendedor): ArrayList<ILocal> {
        TODO("Not yet implemented")
    }

}