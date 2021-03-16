package com.gadgetmart.fragment.forgotpassword


import com.gadgetmart.R
import com.gadgetmart.base.BaseFragment
import com.gadgetmart.databinding.FragmentCreatePasswordBinding


class CreatePasswordFragment : BaseFragment<FragmentCreatePasswordBinding>() {
    override fun getContentView(): Int {
        return R.layout.fragment_create_password
    }

    override fun initView(binding: FragmentCreatePasswordBinding) {
//        binding.btnContinue?.setOnClickListener {
//            activity?.finish()
//        }
        binding.backToLogin?.setOnClickListener {
            activity?.finish()
        }
    }

    override fun initListeners(binding: FragmentCreatePasswordBinding) {

    }

    override fun initPresenters() {

    }

    override fun openInternetDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
