package com.example.momkn.fireStoreDataBase

import com.example.momkn.fireStoreDataBase.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot

class UsersDao {
    companion object{
        fun addUser(user:User,onCompleteListener: OnCompleteListener<Void>){
            val userDoc = DataBase.getUsers()
                .document(user.id?:"")
            userDoc.set(user)
                .addOnCompleteListener(onCompleteListener)
        }
        fun getUser(userId:String,onCompleteListener: OnCompleteListener<DocumentSnapshot>){
            DataBase.getUsers()
                .document(userId)
                .get()
                .addOnCompleteListener(onCompleteListener)


        }
    }

}