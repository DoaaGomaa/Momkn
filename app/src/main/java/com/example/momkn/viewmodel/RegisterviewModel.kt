package com.example.momkn.viewmodel
import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.momkn.base.BaseViewModel
import com.example.momkn.fireStoreDataBase.UsersDao
import com.example.momkn.fireStoreDataBase.model.DataHolder
import com.example.momkn.fireStoreDataBase.model.User
import com.example.momkn.register.NavigatorRegister
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterviewModel(application: Application): BaseViewModel<NavigatorRegister>() {
    val userName = ObservableField<String>()
    val email = ObservableField<String>()
    val password = ObservableField<String>()
    val userNameError = ObservableField<Boolean>()
    val passwordError = ObservableField<Boolean>()
    val emailError = ObservableField<Boolean>()
    val authUser = MutableLiveData<FirebaseUser>()
    private var auth : FirebaseAuth
    init {
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            authUser.value = auth.currentUser
        }    }
    fun signUp(){
        if(isValidData()){
            //show loading and call login api
            showLoading.value = true
            auth.createUserWithEmailAndPassword(email.get()?:"",password.get()?:"")
                .addOnCompleteListener({
                    showLoading.value=false
                    if(it.isSuccessful){
                        val newUser = User()
                        newUser.id = it.result?.user?.uid?:""
                        newUser.name= userName.get()?:""
                        newUser.email = email.get()?:""
                        UsersDao.addUser(newUser, OnCompleteListener {
                            if (it.isSuccessful){
                                DataHolder.dataBaseUser = newUser
                                DataHolder.authUser = auth.currentUser
                            navigaror?.openHome();
                            }else{
                                message.value="faild to register user ..try again later " + it.exception?.localizedMessage
                            }
                        })

                    }else{
                        message.value = it.exception?.localizedMessage
                    }
                })
        }
    }
    fun isValidData() : Boolean{
        var isValid = true;
        if(userName.get().isNullOrBlank()){
            //showError
            userNameError.set(true)
            isValid=false

        } else {
            userNameError.set(false)
        }
        if(email.get().isNullOrEmpty()){
            //showError
            emailError.set(true)
            isValid=false

        } else {
            emailError.set(false)
        }
        if(password.get().isNullOrEmpty()||password.get()?.length ?:0 <6){
            //show Error
            passwordError.set(true)
           message.value = "please enter a valid Data , Password should be more than 6 characters"
            isValid = false

        }else {
            passwordError.set(false)
        }
        return isValid
    }
    fun login(){
        navigaror?.openLogin()
    }

}