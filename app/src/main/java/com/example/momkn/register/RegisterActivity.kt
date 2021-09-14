package com.example.momkn.register
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.momkn.R
import com.example.momkn.base.BaseActivity
import com.example.momkn.databinding.FragmentRegisterBinding
import com.example.momkn.login.LoginActivity
import com.example.momkn.home.MainActivity
import com.example.momkn.viewmodel.RegisterviewModel

class RegisterActivity : BaseActivity<FragmentRegisterBinding, RegisterviewModel>(), NavigatorRegister {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel
        viewModel.navigaror = this
    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_register
    }

    override fun generateViewModel(): RegisterviewModel {
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(RegisterviewModel::class.java)
        return viewModel

    }

    override fun openLogin() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()

    }

    override fun openHome() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}