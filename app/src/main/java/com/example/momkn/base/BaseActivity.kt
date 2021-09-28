package com.example.momkn.base

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.momkn.R

 abstract class BaseActivity<T:ViewDataBinding,V: BaseViewModel<*>> : AppCompatActivity() {
    lateinit var activity: AppCompatActivity;
    lateinit var viewDataBinding: T
    lateinit var viewModel: V


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity=this;
        viewDataBinding=DataBindingUtil.setContentView(this,getLayoutID())
        viewModel = generateViewModel()
        viewModel.message.observe(this, Observer {
            if (it != null){
               showMessage("",it , getString(R.string.ok),DialogInterface.OnClickListener {
                       dialogInterface,i ->  dialogInterface.dismiss()},null,null,true)
            }
        })
        viewModel.showLoading.observe(this, Observer {
            if (it){
                showLoadingDialog(getString(R.string.loading))
            }
            else{
                hideLoadingDialog()
            }
        })
    }
    abstract fun getLayoutID():Int;
    abstract fun generateViewModel():V

    fun showMessage(title:String?,
                    message:String?,
                    PosActionName:String?,
                    posAction: DialogInterface.OnClickListener?,
                    negActionName:String?,
                    negAction:DialogInterface.OnClickListener?,
                    isCancelable:Boolean
    ){
        val dialogBuilder  = AlertDialog.Builder(this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(PosActionName,posAction);
        dialogBuilder.setNegativeButton(negActionName,negAction)
        dialogBuilder.setCancelable(isCancelable)
        dialogBuilder.show()
    }

    fun showMessage(title:Int?,
                    message:Int,
                    PosActionName:Int?,
                    posAction:DialogInterface.OnClickListener?,
                    negActionName:Int?,
                    negAction:DialogInterface.OnClickListener?,
                    isCancelable:Boolean
    ){
        val dialogBuilder  = AlertDialog.Builder(this);
        if (title!=null)
            dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);

        if (PosActionName!=null)
            dialogBuilder.setPositiveButton(PosActionName,posAction);
        if (negActionName!=null)
            dialogBuilder.setNegativeButton(negActionName,negAction)
        dialogBuilder.setCancelable(isCancelable)
        dialogBuilder.show()
    }
    fun hideLoadingDialog(){
        progressDialog?.dismiss()

    }
    var progressDialog: ProgressDialog? =null;

    fun showLoadingDialog(loadingMessage:String?):ProgressDialog?{
        progressDialog= ProgressDialog(this);
        progressDialog?.setMessage(loadingMessage);
        progressDialog?.setCancelable(false);
        progressDialog?.show();
        return progressDialog;
    }
     //

     //
}