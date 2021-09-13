package com.example.momkn.base

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import androidx.fragment.app.Fragment
import com.example.momkn.base.BaseActivity

open class BaseFragment : Fragment() {
    lateinit var activity: BaseActivity<*, *>;

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as BaseActivity<*, *>;
//        activity = (AppCompatActivity)context;
    }

    fun showMessage(title:String?,
                    message:String?,
                    PosActionName:String?,
                    posAction: DialogInterface.OnClickListener?,
                    negActionName:String?,
                    negAction:DialogInterface.OnClickListener?,
                    isCancelable:Boolean
    ){
        activity.showMessage(title,
            message,
            PosActionName,
            posAction,
            negActionName,
            negAction,
            isCancelable)
    }

    fun showMessage(title:Int?,
                    message:Int,
                    PosActionName:Int?,
                    posAction:DialogInterface.OnClickListener?,
                    negActionName:Int?,
                    negAction:DialogInterface.OnClickListener?,
                    isCancelable:Boolean
    ){
        activity.showMessage(title, message, PosActionName,
            posAction, negActionName, negAction, isCancelable)
    }
    fun hideLoadingDialog(){
        activity.hideLoadingDialog()
    }
    fun showLoadingDialog(loadingMessage:String?): ProgressDialog?{
        return activity.showLoadingDialog(loadingMessage)
    }
}