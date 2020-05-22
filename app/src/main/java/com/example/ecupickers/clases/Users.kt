package com.example.ecupickers.clases

import com.example.ecupickers.constantes.EnumReferenciasDB
import com.example.ecupickers.factory.DbReference
import com.example.ecupickers.modelos.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class Users {
    fun traerUser(idUsers: String):User{
        val ref = DbReference.getRef(EnumReferenciasDB.USERS)
        var user=User()
        val qry= ref.orderByKey().equalTo(idUsers)
        val listener=object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                user=p0.getValue(User::class.java)!!

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

        }
        qry.addChildEventListener(listener)
        return user
    }
}