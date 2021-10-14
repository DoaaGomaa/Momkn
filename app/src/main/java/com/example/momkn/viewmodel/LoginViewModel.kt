package com.example.momkn.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.momkn.base.BaseViewModel
import com.example.momkn.fireStoreDataBase.UsersDao
import com.example.momkn.fireStoreDataBase.model.DataHolder
import com.example.momkn.fireStoreDataBase.model.User
import com.example.momkn.login.NavigatorLogin
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(application: Application): BaseViewModel<NavigatorLogin>(){
    val email = ObservableField<String>()
    val password = MutableLiveData<String>()
    val emailError = ObservableField<Boolean>()
    val passwordError = ObservableField<Boolean>()
    val authUser = MutableLiveData<FirebaseUser>()
companion object{
    var auth :FirebaseAuth =FirebaseAuth.getInstance()
}

    init {
        //auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            UsersDao.getUser(auth.currentUser?.uid?:"", OnCompleteListener {
                if (it.isSuccessful) {
                    val dataBaseUser = it.result?.toObject(User::class.java)
                    DataHolder.dataBaseUser = dataBaseUser
                    DataHolder.authUser = auth.currentUser
                    authUser.value = auth.currentUser
                }else{
                    message.value = it.exception?.localizedMessage
                }
            })


        }    }
    fun login(){
        if(isValidData()){
            //show loading and call login api
                showLoading.value = true
            auth.signInWithEmailAndPassword(email.get()?:"",password.value?:"")
                .addOnCompleteListener({
                    showLoading.value=false
                    if(it.isSuccessful){
                        //gotoHomeScreen

                        UsersDao.getUser(auth.currentUser?.uid?:"", OnCompleteListener {
                            if (it.isSuccessful) {
                                val dataBaseUser = it.result?.toObject(User::class.java)
                                DataHolder.dataBaseUser = dataBaseUser
                                DataHolder.authUser = auth.currentUser
                                authUser.value = auth.currentUser
                            }else{
                                message.value = it.exception?.localizedMessage
                            }
                        })

                    }else{
                        message.value = it.exception?.localizedMessage
                    }
                })
        }
    }
    fun register(){
        navigaror?.openRegister()

    }
    fun isValidData() : Boolean{
        var isValid = true;
        if(email.get().isNullOrBlank()){
            //showError
            emailError.set(true)
            isValid=false

        } else {
            emailError.set(false)
        }
        if(password.value.isNullOrBlank()){
            //show Error
            passwordError.set(true)
           // message.value = "please enter a valid Password"
            isValid = false

        }else {
            passwordError.set(false)
        }
        return isValid
    }




}