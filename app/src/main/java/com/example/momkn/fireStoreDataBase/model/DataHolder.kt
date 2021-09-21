package com.example.momkn.fireStoreDataBase.model

import com.google.firebase.auth.FirebaseUser

class DataHolder {
    companion object{
        var dataBaseUser :User?=null
        var authUser : FirebaseUser?=null

    }
}