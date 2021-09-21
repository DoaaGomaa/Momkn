package com.example.momkn.fireStoreDataBase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DataBase {
    companion object{
        val USER_REF = "users"
        val db = Firebase.firestore
        fun getUsers():CollectionReference{
            return db.collection(USER_REF)
        }
    }
}