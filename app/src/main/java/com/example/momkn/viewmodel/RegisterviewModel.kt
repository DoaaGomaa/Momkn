package com.example.momkn.viewmodel

import android.app.Application
import android.util.Log
import android.util.Patterns
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
import java.util.regex.Pattern
import com.google.firebase.firestore.ktx.toObject


class RegisterviewModel(application: Application) : BaseViewModel<NavigatorRegister>() {
    val userName = ObservableField<String>()
    val email = ObservableField<String>()
    val password = ObservableField<String>()
    val selecteUserPosition  = ObservableField<Int>()
    companion object{
        var listOfUser : MutableList<User> = mutableListOf()
        var auth :FirebaseAuth =FirebaseAuth.getInstance()
    }

    var listOfUserName : MutableList<String> = mutableListOf<String>()

    var listOfUserNameM : MutableLiveData<MutableList<String>> = MutableLiveData()
    private val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" + "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                // "(?=.*[a-zA-Z])" +  //any letter
                "(?=.*[@#$%^&+=])" +  //at least 1 special character
                "(?=\\S+$)" +  //no white spaces
                ".{6,}" +  //at least 6 characters
                "$"
    )

    val userNameError = ObservableField<Boolean>()
    val userNameErrorMessage = ObservableField<String>()
    val passwordError = ObservableField<Boolean>()
    val emailErrorMessage = ObservableField<String>()
    val passErrorMessage = ObservableField<String>()

    //
    val emailError = ObservableField<Boolean>()
    val authUser = MutableLiveData<FirebaseUser>()

    init {
        if (auth.currentUser != null) {
            authUser.value = auth.currentUser
        }
        test()

    }

    fun signUp() {
        if (isValidData()) {
            //show loading and call login api
            showLoading.value = true
            auth.createUserWithEmailAndPassword(email.get() ?: "", password.get() ?: "")
                .addOnCompleteListener({
                    showLoading.value = false
                    if (it.isSuccessful) {
                        val newUser = User()
                        newUser.id = it.result?.user?.uid ?: ""
                        newUser.name = userName.get() ?: ""
                        newUser.email = email.get() ?: ""
                         newUser.parentId = listOfUser.get(selecteUserPosition.get()!!).id
                        UsersDao.addUser(newUser, OnCompleteListener {
                            if (it.isSuccessful) {
                                DataHolder.dataBaseUser = newUser
                                DataHolder.authUser = auth.currentUser
                                //    listofParent.add(userName.get()?:"")
                                navigaror?.openHome();
                            } else {
                                message.value =
                                    "faild to register user ..try again later " + it.exception?.localizedMessage
                            }
                        })
                    } else {
                        message.value = it.exception?.localizedMessage
                    }
                })
        }
    }

   fun test() {
      UsersDao.getAllUsersRegister({
            if (it.isSuccessful) {
                for (document in it.getResult()) {
                    Log.i("Registerviewmodel", document.id + " => " + document.data)
                    val user = document.toObject<User>()
                    listOfUser.add(user)
                    Log.i("Registerviewmodel",user.name!!)
                    listOfUserName.add(user.name!!)
                    listOfUserNameM!!.postValue(listOfUserName)
                }
                Log.i("Registerviewmodel", listOfUser.size.toString())
            }
        })
     /*  UsersDao.getAllUsers({
               snapshots, e ->
          *//* if (e != null) {
               //Log.w(TAG, "listen:error", e)
               return@addSnapshotListener
           }*//*

           for (dc in snapshots!!.documentChanges) {
               when (dc.type) {
                   DocumentChange.Type.ADDED -> Log.d("Register", "New city: ${dc.document.data}")
                   DocumentChange.Type.MODIFIED -> Log.d("Register", "Modified city: ${dc.document.data}")
                   DocumentChange.Type.REMOVED -> Log.d("Register", "Removed city: ${dc.document.data}")
               }
           }

       })*/
    }


    fun isValidData(): Boolean {
        var isValid = true;
        //email validation
        if (email.get().isNullOrEmpty()) {
            emailError.set(true)
            emailErrorMessage.set("Field can't be empty")
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.get()).matches()) {
            emailError.set(true)
            emailErrorMessage.set("Please enter a valid email address")
            isValid = false;
        } else {
            emailError.set(false)
            emailErrorMessage.set(null)
            isValid = true;
        }
        //userName validation
        if (userName.get().isNullOrEmpty()) {
            userNameError.set(true)
            userNameErrorMessage.set("Field can't be empty")
            isValid = false
        } else if (userName.get()!!.length > 15) {
            userNameError.set(true)
            userNameErrorMessage.set("Username too long")
            isValid = false
        } else {
            userNameError.set(false)
            userNameErrorMessage.set(null)
            isValid = true
        }
        //password validation
        if (password.get().isNullOrEmpty()) {
            passwordError.set(true)
            passErrorMessage.set("Field can't be empty")
            isValid = false
        } else if (!PASSWORD_PATTERN.matcher(password.get()).matches()) {
            passwordError.set(true)
            passErrorMessage.set("Password too weak must contain $ A 1")
            isValid = false
        } else {
            passwordError.set(false)
            passErrorMessage.set(null)
            isValid = true
        }
        return isValid
    }

    fun login() {
        navigaror?.openLogin()
    }
}