package com.gadgetmart.fragment.register

import com.gadgetmart.R
import com.gadgetmart.base.BaseFragment
import com.gadgetmart.databinding.FragmentSetupAccountDetailBinding
import com.gadgetmart.ui.auth.RegisterSetupActivity
import com.gadgetmart.ui.auth.profile_set_up.SetupProfileFragment
import com.gadgetmart.util.AppUtil

class SetupAccountDetailFragment : BaseFragment<FragmentSetupAccountDetailBinding>() {
    override fun getContentView(): Int {
        return R.layout.fragment_setup_account_detail
    }

    override fun initView(binding: FragmentSetupAccountDetailBinding) {

    }

    override fun initListeners(binding: FragmentSetupAccountDetailBinding) {
        binding.continueButton?.setOnClickListener {
            if(binding.emailet?.text.toString().equals("")){
                AppUtil.showToast(activity!!,"Fields can't be empty")
                binding.emailet.requestFocus()
            }else if (binding.passwordet?.text.toString().equals("")){
                AppUtil.showToast(activity!!,"Fields can't be empty")
                binding.passwordet.requestFocus()
            }else {
                if (activity != null) {
                    (activity as RegisterSetupActivity).changeFrame(SetupProfileFragment())
                }
            }
        }
    }

    override fun initPresenters() {

    }

    override fun openInternetDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
