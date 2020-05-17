package com.example.ecupickers.factory

import com.example.ecupickers.constantes.EnumReferenciasDB
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.E

object DbReference {
    val db: FirebaseDatabase = FirebaseDatabase.getInstance()
    fun getRef(referencia: EnumReferenciasDB): DatabaseReference {
        return when (referencia.rutaDB()) {
            EnumReferenciasDB.CATEGORIA.rutaDB() -> db.reference.child(EnumReferenciasDB.CATEGORIA.rutaDB())
            EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB() -> db.reference.child(EnumReferenciasDB.MIEMBROSALIMENTOS.rutaDB())
            EnumReferenciasDB.CATEGORIASRESTAURANTE.rutaDB() -> db.reference.child(EnumReferenciasDB.CATEGORIASRESTAURANTE.rutaDB())
            EnumReferenciasDB.LOCALES.rutaDB() -> db.reference.child(EnumReferenciasDB.LOCALES.rutaDB())
            EnumReferenciasDB.MENUS.rutaDB() -> db.reference.child(EnumReferenciasDB.MENUS.rutaDB())
            EnumReferenciasDB.MIEMBROSCONSUMIDORES.rutaDB() -> db.reference.child(EnumReferenciasDB.MIEMBROSCONSUMIDORES.rutaDB())
            EnumReferenciasDB.MIEMBROSDELIVERY.rutaDB() -> db.reference.child(EnumReferenciasDB.MIEMBROSDELIVERY.rutaDB())
            EnumReferenciasDB.PEDIDOS.rutaDB() -> db.reference.child(EnumReferenciasDB.PEDIDOS.rutaDB())
            EnumReferenciasDB.PRODUCTOS.rutaDB() -> db.reference.child(EnumReferenciasDB.PRODUCTOS.rutaDB())
            EnumReferenciasDB.USERS.rutaDB() -> db.reference.child(EnumReferenciasDB.USERS.rutaDB())
            EnumReferenciasDB.ROOT.rutaDB() -> db.reference.root
            else->db.reference.root
        }
    }
}