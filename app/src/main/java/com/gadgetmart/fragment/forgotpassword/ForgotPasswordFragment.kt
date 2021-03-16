package com.gadgetmart.fragment.forgotpassword

import android.os.Bundle
import com.gadgetmart.R
import com.gadgetmart.base.BaseFragment
import com.gadgetmart.databinding.FragmentForgotPasswordBinding
import com.gadgetmart.ui.auth.ForgotPasswordActivity
import com.gadgetmart.ui.auth.otp_verification.OtpVerificationFragment

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {
    override fun getContentView(): Int {
        return R.layout.fragment_forgot_password
    }

    override fun initView(binding: FragmentForgotPasswordBinding) {
        binding.continueButton?.setOnClickListener {
            if (activity!=null)
            {
                val bundle = Bundle()
                bundle.putString("type","forgotpassword")
                val fragment =
                    OtpVerificationFragment()
                fragment.arguments = bundle
                (activity as ForgotPasswordActivity).changeFrame(fragment)
            }
        }

        binding.backToLogin?.setOnClickListener {
            activity?.finish()
        }
    }

    override fun initListeners(binding: FragmentForgotPasswordBinding) {

    }

    override fun initPresenters() {

    }

    override fun openInternetDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
