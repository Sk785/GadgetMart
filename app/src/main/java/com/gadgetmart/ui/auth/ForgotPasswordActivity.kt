package com.gadgetmart.ui.auth

import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.ActivityForgotPasswordBinding
import com.gadgetmart.fragment.forgotpassword.ForgotPasswordFragment

class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>() {

    private var BACK_STACK_ROOT_TAG:String = "forgotpassword"
    override fun getContentView(): Int {
        return R.layout.activity_forgot_password
    }

    override fun init(binding: ActivityForgotPasswordBinding) {
        defaultFrame(ForgotPasswordFragment())
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun defaultFrame(fragment: Fragment) {

        val fm = supportFragmentManager
        //fm.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.forgotframe, fragment)
        fragmentTransaction.addToBackStack(null);
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
        fragmentTransaction.replace(R.id.forgotframe, fragment)
        fragmentTransaction.addToBackStack(BACK_STACK_ROOT_TAG)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
