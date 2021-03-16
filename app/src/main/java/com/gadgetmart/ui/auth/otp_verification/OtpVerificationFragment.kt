package com.gadgetmart.ui.auth.otp_verification

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.gadgetmart.R
import com.gadgetmart.base.BaseFragment
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.OtpVerificationFragmentBinding
import com.gadgetmart.ui.auth.RegisterSetupActivity
import com.gadgetmart.ui.auth._common.AuthResult
import com.gadgetmart.ui.auth.phone_number_verification.PhoneVerificationContract
import com.gadgetmart.ui.auth.phone_number_verification.PhoneVerificationPresenter
import com.gadgetmart.ui.auth.phone_number_verification.PhoneVerificationRequest
import com.gadgetmart.ui.auth.profile_set_up.SetupProfileFragment
import com.gadgetmart.ui.home.HomeActivity
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.Constants
import com.gadgetmart.util.PreferenceManager
import com.gadgetmart.util.custom.CustomProgressDialog
import com.thrivecom.ringcaptcha.RingcaptchaAPIController
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OtpVerificationFragment : BaseFragment<OtpVerificationFragmentBinding>(),
    PhoneVerificationContract {

    //    private var type = ""
    private var otpNumber = ""
    private var countryCode = ""
    private var phoneNumber = ""
    private var controller: RingcaptchaAPIController? = null
    private var phoneVerificationPresenter: PhoneVerificationPresenter? = null
    private val intentExtraCountryCode = "extra_country_code"
    private val intentExtraPhoneNumber = "extra_phone_number"
    private var timer: CountDownTimer? = null
    private var customProgressDialog: CustomProgressDialog? = null
    private var email = ""
    private var socialId = ""
    private var socialType = ""
    private var name = ""
    private var type = ""
    var isLogin = ""

    val bundle = Bundle()
    var total = 60000L;
    var remainTotal = 0L;
    lateinit var  prefernce:SharedPreferences
    lateinit var ed:SharedPreferences.Editor
    var binding:OtpVerificationFragmentBinding?=null

    fun newInstance(bundle: Bundle): OtpVerificationFragment {
        val fragment =
            OtpVerificationFragment()

        fragment.arguments = bundle

        return fragment
    }

    override fun getContentView(): Int {
        return R.layout.otp_verification_fragment
    }

    override fun initView(binding: OtpVerificationFragmentBinding) {
        val bundle = arguments
        prefernce=activity!!.getSharedPreferences("long", Context.MODE_PRIVATE)
        ed=prefernce.edit()
        customProgressDialog = CustomProgressDialog(activity!!)
        this.binding=binding
        AppUtil.showSoftKeyboard(activity!!)
        binding.timer.visibility = View.VISIBLE


        binding.resentOtpLayout.isEnabled = false
        if (bundle != null) {
            countryCode = bundle.getString(Constants.countryCode)!!
            phoneNumber = bundle.getString(Constants.phoneNumber)!!
            email = bundle.getString(Constants.email)!!
            socialId = bundle.getString(Constants.socialId)!!
            socialType = bundle.getString(Constants.type)!!
            name=bundle.getString("name")!!
            type=bundle.getString("type1")!!
            isLogin=bundle.getString("isLogin")!!
            binding.otpPhoneNumberTextView.text =
                resources.getString(R.string.otp_sent_to) + " " + countryCode + " " + phoneNumber
        }
        controller = RingcaptchaAPIController(AppUtil.ringCaptchaAppKey)
        context!!.startService(Intent(context,BradcastService::class.java))

    }

    override fun onResume() {
        super.onResume()
        context!!.registerReceiver(br, IntentFilter(BradcastService.COUNTDOWN_BR));
    }

    override fun initPresenters() {
        phoneVerificationPresenter = PhoneVerificationPresenter(activity!!, this)
    }

    private val br: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateGUI(intent) // or whatever method used to update your GUI fields
        }
    }

    private fun updateGUI(intent: Intent) {
        if (intent.extras != null) {
            val millisUntilFinished = intent.getLongExtra("countdown", 0)
            val timeLeft = millisUntilFinished / 1000
            if (timeLeft.toInt() < 10) {
                stopTimer(timeLeft)
            } else {
                binding!!.timer.text = "$timeLeft"
            }
        }
    }

    private fun stopTimer(timeLeft: Long) {
        binding!!.timer.text = "0" + "$timeLeft"
        binding!!.timer.visibility = View.GONE
        binding!!.resendOtpText.setTextColor(
            ContextCompat.getColor(
                activity!!,
                R.color.colorDarkText
            )
        )
        binding!!.resendOtpText.text = resources.getString(R.string.resend_otp_now)
        binding!!.resentOtpLayout.isEnabled = true
        context!!.stopService(Intent(context, BradcastService::class.java))
    }

    override fun onPause() {
        super.onPause()
        context!!.unregisterReceiver(br)
    }


    override fun onDestroy() {
        context!!.stopService(Intent(context, BradcastService::class.java))
        super.onDestroy()
    }


    override fun onStart() {
        super.onStart()
      //  context!!.startService(Intent(context, BradcastService::class.java))
       // callResendTimer()
    }
    fun callResendTimer(){
        if(prefernce.getLong("long", 0L)!=0L){
            startTimer(binding!!, prefernce.getLong("long", 0L))

        }else{
            startTimer(binding!!, total)

        }
        timer!!.start()
    }


    fun startTimer(binding: OtpVerificationFragmentBinding, total: Long) {
        timer = object : CountDownTimer(total, 1000) {
            override fun onFinish() {
                timer?.cancel()
                ed.putLong("long", 0L)
                ed.commit()
                binding.timer.visibility = View.GONE
                binding.resendOtpText.setTextColor(
                    ContextCompat.getColor(
                        activity!!,
                        R.color.colorDarkText
                    )
                )
                binding.resendOtpText.text = resources.getString(R.string.resend_otp_now)
                binding.resentOtpLayout.isEnabled = true

            }

            override fun onTick(millisuntilfinish: Long) {
                ed.putLong("long", millisuntilfinish)
                ed.commit()
                remainTotal=millisuntilfinish
                val timeLeft = millisuntilfinish / 1000
                if (timeLeft.toInt() < 10) {
                    binding.timer.text = "0" + "$timeLeft"
                } else {
                    binding.timer.text = "$timeLeft"
                }
            }
        }


    }


    override fun initListeners(binding: OtpVerificationFragmentBinding) {

        binding.resentOtpLayout.setOnClickListener { onResendOtpLinkTapped(binding) }

        binding.continueButton.setOnClickListener {
            onSendOtpButtonTapped()
        }

        binding.otpView.setOtpCompletionListener { otp -> otpNumber = otp!! }
    }

    private fun onSendOtpButtonTapped() {
        if (!AppUtil.isFieldEmpty(otpNumber)) {
            showToast(R.string.error_msg_otp_number_empty)
            return
        }

        verifyOtp()
    }

    private fun onResendOtpLinkTapped(binding: OtpVerificationFragmentBinding) {
        ed.putLong("long", 0L)
        ed.commit()
        customProgressDialog?.dialogShow()
        sendOtp(countryCode, phoneNumber, binding)

    }

    private fun verifyOtp() {
        AppUtil.hideSoftKeyboard(activity!!, activity?.currentFocus!!)
        customProgressDialog?.dialogShow()

        verifyOtp(otpNumber, phoneNumber)

    }

    override fun onPhoneNumberVerified(
        authResult: AuthResult?,
        profileStatus: Int,
        message: String
    ) {

        if (activity != null) {
            if(isLogin=="yes") {


                when (profileStatus) {
                    0 ->

                        (activity as RegisterSetupActivity).changeFrame(
                            SetupProfileFragment().newInstance(
                                bundle
                            )
                        )
                    1 -> HomeActivity.start(context)
                    2 -> {
                        AppUtil.firebaseEvent(activity!!, "error", "login_error", message)

                        showToast(message)

                    }

                }
            }else{
                var i=Intent()
                i.putExtra("phone", phoneNumber)
                activity!!.setResult(Activity.RESULT_OK, i)
                activity!!.finish()
            }

        }
    }

    override fun onPhoneNumberVerificationFailed(message: String) {
        AppUtil.firebaseEvent(activity!!, "error", "error_events", message)

        showToast(message)
    }

    override fun onStop() {
        super.onStop()
        //timer?.cancel()
    }

    override fun openInternetDialog() {
    }

     fun sendOtp(code: String, phone: String, binding: OtpVerificationFragmentBinding) {
        try {

            ApiClientGenerator
                .getClient()!!
                .sendOTP(
                    phone, type
                )
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        customProgressDialog!!.dialogDismiss()

                        if (response.code() < 200 || response.code() >= 300) {
                            return
                        }

                        val response = JSONObject(response.body()?.string()!!)
                        Log.e("response", response.toString())
                        if (response.getString("status") == "1") {
                            //callResendTimer()
                            context!!.startService(Intent(context,BradcastService::class.java))
                            binding!!.resentOtpLayout.isEnabled = false
                            binding.timer.visibility = View.VISIBLE
                            binding.resendOtpText.text = resources.getString(R.string.resend_otp)
                            binding.timer.setTextColor(
                                ContextCompat.getColor(
                                    activity!!,
                                    R.color.grey_approx
                                )
                            )
                            binding.resendOtpText.setTextColor(
                                ContextCompat.getColor(
                                    activity!!,
                                    R.color.grey_approx
                                )
                            )
                            // finish()
                        } else {
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseBody?>,
                        t: Throwable
                    ) {
                        customProgressDialog!!.dialogDismiss()

                        //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun verifyOtp(code: String, phone: String) {
        try {

            ApiClientGenerator
                .getClient()!!
                .verifyOTP(
                    code, phone
                )
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        customProgressDialog!!.dialogDismiss()

                        if (response.code() < 200 || response.code() >= 300) {
                            return
                        }

                        val response = JSONObject(response.body()?.string()!!)
                        Log.e("response", response.toString())
                        if (response.getString("status") == "1") {
                            bundle.putString(Constants.email, email)
                            bundle.putString("name", name)

                            bundle.putString(Constants.socialId, socialId)
                            bundle.putString(Constants.type, socialType)
                            bundle.putString(Constants.countryCode, countryCode)
                            bundle.putString(Constants.phoneNumber, phoneNumber)
                            if (socialType == "") {
                                phoneVerificationPresenter?.validateFields(
                                    PhoneVerificationRequest()
                                        .setCountryCode(countryCode)
                                        .setDeviceToken(PreferenceManager(context).getOneSignalNotificationId())
                                        .setPhoneNumber(phoneNumber)
                                )

                            } else {

                                (activity as RegisterSetupActivity).changeFrame(
                                    SetupProfileFragment().newInstance(
                                        bundle
                                    )
                                )
                            }
                            // finish()
                        } else {
                            AppUtil.firebaseEvent(
                                activity!!, "error", "error_events", response.getString(
                                    "message"
                                )
                            )

                            showToast(response.getString("message"))
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseBody?>,
                        t: Throwable
                    ) {
                        customProgressDialog!!.dialogDismiss()

                        //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}


