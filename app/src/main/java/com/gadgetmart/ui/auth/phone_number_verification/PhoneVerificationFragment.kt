package com.gadgetmart.ui.auth.phone_number_verification


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.gadgetmart.R
import com.gadgetmart.base.BaseFragment
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.RegisterationFragmentBinding
import com.gadgetmart.ui.auth.RegisterSetupActivity
import com.gadgetmart.ui.auth.otp_verification.OtpVerificationFragment
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.Constants
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.PreferenceManager
import com.gadgetmart.util.custom.CustomProgressDialog
import com.thrivecom.ringcaptcha.RingcaptchaAPIController
import com.thrivecom.ringcaptcha.RingcaptchaService
import com.thrivecom.ringcaptcha.lib.handlers.RingcaptchaHandler
import com.thrivecom.ringcaptcha.lib.models.RingcaptchaResponse
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PhoneVerificationFragment : BaseFragment<RegisterationFragmentBinding>() {



        fun newInstance(bundle: Bundle): PhoneVerificationFragment {
            val fragment =
                PhoneVerificationFragment()

            fragment.arguments = bundle
            return fragment

//
//        fun start(context: Context?) {
//            val intent = Intent(context, SignInActivity::class.java)
//            context!!.startActivity(intent)
//        }

    }
    private var countryCode : String?  = ""
    private var countryNameCode : String?  = "IN"
    private var countryName : String?  = "India"
    private var controller: RingcaptchaAPIController? = null
    private var customDialog : CustomProgressDialog? = null
    private var email = ""
    private var socilaId = ""
    private var socilaType = ""
    private var name = ""
    private var type = ""
var isLogin=""

    lateinit var  prefernce: SharedPreferences
    lateinit var ed: SharedPreferences.Editor



    override fun getContentView(): Int {
        return R.layout.registeration_fragment
    }

    override fun initListeners(binding: RegisterationFragmentBinding) {
        customDialog = CustomProgressDialog(activity!!)
        prefernce=activity!!.getSharedPreferences("long", Context.MODE_PRIVATE)
        ed=prefernce.edit()

        binding.continueButton?.setOnClickListener {

            onContinueButtonTapped(binding)

            /*if(binding.countryCodeEditText?.text.toString().equals("")){
                AppUtil.showToast(this,"Fields can't be empty")
                binding.countryCodeEditText.requestFocus()

            }else if (binding.phoneNumberEditText?.text.toString().equals("")){
                AppUtil.showToast(this,"Fields can't be empty")
                binding.phoneNumberEditText.requestFocus()

            }else {
                HomeActivity.start(this)
                finish()
            }*/
        }

        binding.countryCodeEditText.setOnClickListener {
            binding.ccpCountryCode?.launchCountrySelectionDialog(countryNameCode) }
        binding.countryCodeEditText.isEnabled=false
        binding.ccpCountryCode?.setOnCountryChangeListener {
            getCountryCode(binding)
        }
        val bundle = arguments

        if (bundle != null) {
            email = bundle.getString(Constants.email)!!
            socilaId = bundle.getString(Constants.socialId)!!
            socilaType = bundle.getString(Constants.type)!!
            name=bundle.getString("name")!!
            type=bundle.getString("type1")!!
            isLogin=bundle.getString("isLogin")!!



        }
        Log.e("social data",email+" "+socilaId+" "+socilaType)
      /*  binding.backIcon.setOnClickListener {
            activity!!.finish()
        }*/

    }

    override fun onStart() {
        super.onStart()
        ed.putLong("long",0L)
        ed.commit()
    }
    private fun getCountryCode(binding: RegisterationFragmentBinding){
//        countryCodeName = binding.ccpCountryCode.selectedCountryNameCode
//        Log.e("Name ::::: ", "" + countryCodeName)
        countryNameCode = binding.ccpCountryCode.selectedCountryNameCode
        countryName = binding.ccpCountryCode.selectedCountryName
        PreferenceManager().getInstance(activity).setCountryNameCode(countryNameCode)
        PreferenceManager().getInstance(activity).setCountryName(countryName)
        countryCode = binding.ccpCountryCode.selectedCountryCodeWithPlus
//        countryName = binding.ccpCountryName.selectedCountryName
        countryCode.let { binding.countryCodeEditText?.setText(it) }
    }

    private fun onContinueButtonTapped(binding: RegisterationFragmentBinding) {

        val countryCode = binding.countryCodeEditText?.text.toString()
        val phoneNumber = binding.phoneNumberEditText?.text.toString()

        if (TextUtils.isEmpty(countryCode)){
            showToast(R.string.error_msg_country_code)
            return
        }
        if (!AppUtil.isFieldEmpty(phoneNumber)){
            showToast(R.string.error_msg_phone_number_empty)
            return
        }
        if(!AppUtil.isValidPhoneNumber(phoneNumber)){
            showToast(R.string.error_msg_phone_number)
            return
        }
        customDialog?.dialogShow()
        Log.e("SMS SEND ::::", "" + countryCode + ".." + phoneNumber)
       sendOtp(countryCode , phoneNumber)
//            controller!!.sendCaptchaCodeToNumber(
//                context,
//                countryCode + phoneNumber,
//                RingcaptchaService.SMS,
//                object : RingcaptchaHandler {
//
//                    //Called when the response is successful
//                    override fun onSuccess(response: RingcaptchaResponse) {
//                        customDialog?.dialogDismiss()
//
///*
//                    //Handle SMS reception automatically (only valid for verification)
//                    RingcaptchaAPIController.setSMSHandler { s, s1 ->
//
//                        //Only called when SMS reception was detected
//                        //Automatically verify PIN code
//
//                        true
//                    }*/
////                    replaceFragment(
////                        OtpVerificationFragment().newInstance(
////                            countryCode,
////                            phoneNumber
////                        ),
////                        "OtpVerificationFragment",
////                        addToBackStack = false,
////                        shouldAnimateTransition = true
////                    )
//                    }
//
//                    //Called when the response is unsuccessful
//                    override fun onError(e: Exception) {
//                        customDialog?.dialogDismiss()
//                        Log.e("SMS error ::: ", "  " + e.localizedMessage)
//                        //Display an error to the user
//                        e.printStackTrace()
//                    }
//                },
//                AppUtil.ringCaptchaApiKey
//            )
    }

    override fun initView(binding: RegisterationFragmentBinding) {
//        controller = RingcaptchaAPIController(AppUtil.ringCaptchaAppKey)
   countryCode = binding.ccpCountryCode?.selectedCountryCodeWithPlus

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )


//        binding.registerTV.setOnClickListener {
//            RegisterSetupActivity.start(this)
//        }
//        binding.forgotpasswordtv.setOnClickListener {
//            val intent = Intent(this, ForgotPasswordActivity::class.java)
//            startActivity(intent)
//        }
    }

    override fun initPresenters() {
    }

    override fun openInternetDialog() {
    }

    //-------------------------Send otp----------------
    private fun sendOtp(code: String,phone: String) {
        try {

            ApiClientGenerator
                .getClient()!!
                .sendOTP(
                    phone,type
                )
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        customDialog!!.dialogDismiss()

                        if (response.code() < 200 || response.code() >= 300) {
                            return
                        }

                        val response = JSONObject(response.body()?.string()!!)
                        Log.e("response",response.toString())
                        if (response.getString("status") == "1") {

                            val bundle = Bundle()
                            bundle.putString("name", name)
                            bundle.putString("type1", type)


                            bundle.putString(Constants.email, email)
                            bundle.putString(Constants.socialId, socilaId)
                            bundle.putString(Constants.type, socilaType)
                            bundle.putString(Constants.countryCode, code)
                            bundle.putString(Constants.phoneNumber, phone)
                            bundle.putString("isLogin", isLogin)




                            (activity as RegisterSetupActivity)
                                .clearFrame(
                                    OtpVerificationFragment().newInstance(
                                        bundle
                                    )
                                )
                            // finish()
                        } else {
                            AppUtil.firebaseEvent(activity!!,"error","error_events",response.getString("message"))
                            showToast(response.getString("message"))

                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseBody?>,
                        t: Throwable
                    ) {
                        customDialog!!.dialogDismiss()
                        showToast("Network connection error")

                        //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}