package com.example.momkn.login
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.momkn.R
import com.example.momkn.base.BaseActivity
import com.example.momkn.databinding.FragmentLogonBinding
import com.example.momkn.register.RegisterActivity
import com.example.momkn.home.MainActivity
import com.example.momkn.viewmodel.LoginViewModel

class LoginActivity : BaseActivity<FragmentLogonBinding, LoginViewModel>(), NavigatorLogin {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.authUser.observe(this, Observer {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        })
        viewDataBinding.vm = viewModel
        viewModel.navigaror=this

    }

    override fun openRegister() {
       startActivity(Intent(this,RegisterActivity::class.java))
        finish()

    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_logon
    }

    override fun generateViewModel(): LoginViewModel {
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(LoginViewModel::class.java)
        return viewModel

    }
}