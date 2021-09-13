package com.example.momkn.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

 open class BaseViewModel<N> : AndroidViewModel(Application()){
     var navigaror :N ? = null
    val showLoading = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()
}
