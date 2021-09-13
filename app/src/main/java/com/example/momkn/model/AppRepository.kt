package com.example.momkn.model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class AppRepository(application: Application) {
    lateinit var firebaseAuth:FirebaseAuth


}
