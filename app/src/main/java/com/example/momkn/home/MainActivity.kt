package com.example.momkn.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.momkn.R
import com.example.momkn.base.BaseActivity
import com.example.momkn.databinding.ActivityMainBinding
import com.example.momkn.viewmodel.HomeViewModel

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
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(
            HomeViewModel::class.java)
        return viewModel

    }
}