package com.example.momkn.register
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.momkn.R
import com.example.momkn.base.BaseActivity
import com.example.momkn.databinding.FragmentRegisterBinding
import com.example.momkn.fireStoreDataBase.model.DataHolder
import com.example.momkn.login.LoginActivity
import com.example.momkn.home.MainActivity
import com.example.momkn.viewmodel.RegisterviewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : BaseActivity<FragmentRegisterBinding, RegisterviewModel>(), NavigatorRegister {
    var adapter :ArrayAdapter<String> ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel
        viewModel.navigaror = this
        viewModel.listOfUserNameM!!.observe(this,{
            adapter = ArrayAdapter(this@RegisterActivity ,R.layout.spinner_item,it
            )
            adapter!!.notifyDataSetChanged()
            viewDataBinding.spinnerParent.setAdapter(adapter)
        })



     // parentId = viewModel.listOfUser.get(viewDataBinding.spinnerParent.selectedItemPosition).parentId





    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_register
    }

    override fun generateViewModel(): RegisterviewModel {
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(RegisterviewModel::class.java)
        return viewModel

    }

    override fun openLogin() {
        //startActivity(Intent(this,LoginActivity::class.java))
        finish()

    }

    override fun openHome() {

        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
    fun register(view: View) {
       viewModel.signUp()
    }

}