package com.gadgetmart.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.ActivityRegisterSetupBinding
import com.gadgetmart.ui.auth.phone_number_verification.PhoneVerificationFragment
import com.gadgetmart.util.Constants

class RegisterSetupActivity : BaseActivity<ActivityRegisterSetupBinding>() {
    override fun init(binding: ActivityRegisterSetupBinding) {
        Log.e("social data",intent.extras!!.getString(Constants.email)+" "+intent.extras!!.getString(Constants.socialId)+" "+
                intent.extras!!.getString(Constants.type))
        val bundle = Bundle()

        bundle.putString(Constants.email, intent.extras!!.getString(Constants.email))
        bundle.putString("name", intent.extras!!.getString("name"))
        bundle.putString("type1", intent.extras!!.getString("type1"))
        bundle.putString("isLogin", intent.extras!!.getString("isLogin"))



        bundle.putString(Constants.socialId, intent.extras!!.getString(Constants.socialId))
        bundle.putString(Constants.type, intent.extras!!.getString(Constants.type))


        defaultFrame(PhoneVerificationFragment().newInstance(bundle))
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private var BACK_STACK_ROOT_TAG: String = "profilesetup"

    /*companion object {
        fun start(context: Context?) {
            val intent = Intent(context, RegisterSetupActivity::class.java)
            context!!.startActivity(intent)
        }

    }*/

    override fun getContentView(): Int {
        return R.layout.activity_register_setup
    }


    fun defaultFrame(fragment: Fragment) {

        val fm = supportFragmentManager
        //fm.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.loginsignupframe, fragment)
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit()
    }

    fun clearFrame(fragment: Fragment) {

        val fm = supportFragmentManager
        //fm.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.loginsignupframe, fragment)
        fragmentTransaction.commit()
    }

    fun changeFrame(fragment: Fragment) {

        val fm = supportFragmentManager
//        fm.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.slide_in_left, R.anim.slide_out_left,
            R.anim.slide_out_right, R.anim.slide_in_right
        )
        fragmentTransaction.replace(R.id.loginsignupframe, fragment)
        fragmentTransaction.addToBackStack(BACK_STACK_ROOT_TAG)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()

//        AppUtil.hideSoftKeyboard(this, this.currentFocus!!)
        /* if (backStackEntryCount == 0){
             val alertDialog = AlertDialog.Builder(this@ProfileSetupActivity)
             val dialog = alertDialog.create()
             dialog.setCancelable(true)
             if (dialog.window != null) {
                 dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
             }
             dialog.setTitle("Diet Tracking")
             dialog.setMessage("Do you want to Exit?")
             dialog.setButton(
                 DialogInterface.BUTTON_POSITIVE, "Yes"
             ) { dialogInterface, i ->
                 dialogInterface.dismiss()
                 super.onBackPressed()
             }
             dialog.setButton(
                 DialogInterface.BUTTON_NEGATIVE, "No"
             ) { dialogInterface, i -> dialogInterface.dismiss() }

             dialog.show()
         }else {
             super.onBackPressed()
         }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
            var i=Intent()
            i.putExtra("phone",data!!.extras!!.getString("phone"))
            setResult(Activity.RESULT_OK,i)
            finish()
        }
    }
}
