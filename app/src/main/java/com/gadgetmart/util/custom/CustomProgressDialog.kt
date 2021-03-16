package com.gadgetmart.util.custom

import android.app.Activity
import android.app.Dialog
import com.gadgetmart.R

class CustomProgressDialog(var activity: Activity) {
    var mDialog: Dialog?= null
    init {
        mDialog = Dialog(activity, R.style.TransparentProgressDialog)
        mDialog?.setCancelable(false)
        mDialog?.setContentView(R.layout.progress_dislog_layout)
    }

    fun dialogShow(){
        try{
            if (mDialog!=null){
                mDialog?.show()
            }else{
                CustomProgressDialog(activity)
                dialogShow()
            }
        }catch (e:Exception){}
    }

    fun dialogDismiss(){
        try {
            if (mDialog!=null && !activity.isFinishing){
                mDialog?.dismiss()
            }
        }catch (e:java.lang.Exception){}
    }
}