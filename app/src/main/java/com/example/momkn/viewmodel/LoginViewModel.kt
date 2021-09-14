package com.example.momkn.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.momkn.base.BaseViewModel
import com.example.momkn.login.NavigatorLogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(application: Application): BaseViewModel<NavigatorLogin>(){
    val email = ObservableField<String>()
    val password = MutableLiveData<String>()
    val emailError = ObservableField<Boolean>()
    val passwordError = ObservableField<Boolean>()
    val authUser = MutableLiveData<FirebaseUser>()

    private var auth :FirebaseAuth
    init {
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            authUser.value = auth.currentUser
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
                        authUser.value = auth.currentUser
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
        if(password.value.isNullOrEmpty()){
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