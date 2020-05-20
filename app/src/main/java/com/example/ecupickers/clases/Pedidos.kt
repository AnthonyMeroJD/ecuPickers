package com.example.ecupickers.clases

import com.example.ecupickers.constantes.EnumCamposDB
import com.example.ecupickers.constantes.EnumEstado
import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.factory.DbReference
import com.google.firebase.database.Query

/*
* Esta clase administra parcialmente el nodo PEDIDOS
* ya que solo se toma pedidos  y se cambia el estado del pedido estados
* Traer pedidos traes ,los pedidos segun el estado que se le envie como parametro |traerPedidos|
* Cambiar estado, cambia el estado del pedido que se le mande como parametro |cambiarEstadoPedido|
* */
class Pedidos {

    fun traerPedidos(idLocal: String, idCiudad: String, estado: EnumEstado): Query {
        val ref = DbReference.getRef(EnumReferenciasDB.PEDIDOS)
        return ref.child(idCiudad).child(idLocal).orderByChild("${EnumCamposDB.ESTADO.getCampos()}")
            .equalTo(estado.getEstado())
    }

    fun cambiarEstadoPedido(
        idLocal: String,
        idCiudad: String,
        idPedidos: String,
        estado: EnumEstado
    ) {
        val childUpdates = HashMap<String, Any>()
        val ref = DbReference.getRef(EnumReferenciasDB.ROOT)
        childUpdates.put("${EnumReferenciasDB.PEDIDOS.rutaDB()}" +
                "/${idCiudad}/${idLocal}/${idPedidos}/" +
                "${EnumCamposDB.ESTADO.getCampos()}",estado.getEstado())
        ref.updateChildren(childUpdates)
    }
}