package com.gadgetmart.fragment.register


import android.os.Bundle
import com.gadgetmart.R
import com.gadgetmart.base.BaseFragment
import com.gadgetmart.databinding.RegisterationFragmentBinding
import com.gadgetmart.ui.auth.RegisterSetupActivity
import com.gadgetmart.ui.auth.otp_verification.OtpVerificationFragment
import com.gadgetmart.util.AppUtil


class RegisterScreenFragment1 :  BaseFragment<RegisterationFragmentBinding>(){

    override fun getContentView(): Int {
        return R.layout.registeration_fragment
    }

    override fun initView(binding: RegisterationFragmentBinding) {


    }

    override fun initListeners(binding: RegisterationFragmentBinding) {

//        binding.backToLogin.setOnClickListener {
//            if (activity != null)
//                (activity as RegisterSetupActivity).onBackPressed()
//        }
        binding.continueButton.setOnClickListener {
            if (binding.phoneNumberEditText?.text.toString().equals("")) {
                AppUtil.showToast(activity!!, "Field can't be empty")
                binding.phoneNumberEditText.requestFocus()
            } else {
                val bundle = Bundle()
                bundle.putString("type", "register")
                val fragment =
                    OtpVerificationFragment()
                fragment.arguments = bundle
                (activity as RegisterSetupActivity).changeFrame(fragment)
            }
        }
    }

    override fun initPresenters() {
    }

    override fun openInternetDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}


