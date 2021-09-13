package com.example.momkn.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.momkn.R
import com.example.momkn.base.BaseActivity
import com.example.momkn.databinding.ActivityMainBinding
import com.example.momkn.viewmodel.HomeViewModel
import com.example.momkn.viewmodel.LoginViewModel

class MainActivity : BaseActivity<ActivityMainBinding, HomeViewModel>(),NavigatorHome{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewDataBinding.vm = viewModel
    }

    override fun getLayoutID(): Int {
      return R.layout.activity_main
    }

    override fun generateViewModel(): HomeViewModel {
       return ViewModelProvider(this).get(HomeViewModel::class.java)
    }
}