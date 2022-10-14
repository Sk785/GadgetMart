package com.gadgetmart.ui.coupon

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.TextView
import com.facebook.login.LoginManager
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.CouponLayoutBinding
import com.gadgetmart.databinding.HomeActivityBinding
import com.gadgetmart.databinding.ReviewListScreenBinding
import com.gadgetmart.ui.auth._common.AuthResult
import com.gadgetmart.ui.my_review.ReviewContract
import com.gadgetmart.ui.splash.WelcomeActivity
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.PreferenceManager
import com.gadgetmart.util.custom.CustomProgressDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.no_data_found.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CouponActivity : BaseActivity<CouponLayoutBinding>() {
    lateinit var binding: CouponLayoutBinding
    private var customProgressDialog: CustomProgressDialog? = null

    override fun getContentView(): Int {
        return R.layout.coupon_layout


    }

    override fun init(binding: CouponLayoutBinding) {
        this.binding = binding

        binding.toolbarID.backIcon?.setOnClickListener { finish() }
        binding.toolbarID.toolbarTitleTextView?.text = "Coupon Apply"
        binding.toolbarID.toolbarOhnikImageView.visibility = View.GONE
        binding.toolbarID.toolbarCartIcon.visibility = View.GONE

        customProgressDialog = CustomProgressDialog(this)
        setListners()

    }

    fun setListners() {
        binding.btnApply.setOnClickListener {
            if (binding.etSearch.text.toString().isNotEmpty()) {
                customProgressDialog!!.dialogShow()
                applyCoupon("", binding.etSearch.text.toString());

            }
        }
    }


    //-------------------------Send otp----------------
    private fun applyCoupon(id: String, coupon: String) {
        try {

            ApiClientGenerator
                .getClient()!!
                .getOfferDetail(
                    coupon, id,
                    ContextUtils.getAuthToken(this)
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
                        Log.e("copupon response", response.toString())
                        if (response.getString("status") == "1") {
                            var obj = response.getJSONObject("offer")

                            val i = Intent()
                            i.putExtra("name", obj.getString("title"))
                            i.putExtra("id", obj.getString("id"))
                            i.putExtra("amount", obj.getString("amount"))
                            i.putExtra("minmum_amount", obj.getString("minmum_amount"))
                            i.putExtra("coupon_code",obj.getString("selected_coupon_code"))
                            setResult(Activity.RESULT_OK, i)
                            finish()


                        } else {
                            if (response.getString("message")!=null) {
                                if (response.getString("message").isNotEmpty()) {
                                    if (response.getString("message").equals("null")){
                                        message(binding, "Invalid promo code")
                                    }else {
                                        message(binding, response.getString("message"))
                                    }
                                }else{
                                    message(binding,"Invalid promo code")
                                }
                            }else{
                                message(binding,"Invalid promo code")
                            }
                            AppUtil.firebaseEvent(applicationContext,"error","error_events",response.getString("message")
                            )
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


    private fun message(binding: CouponLayoutBinding, message: String) {


        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.coupon_message_layout)
        if (dialog.window != null) {
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        }
        val btncancel = dialog.findViewById(R.id.cancelbtn) as TextView
        btncancel.text="Ok"
        val dialog_msg = dialog.findViewById(R.id.dialog_msg) as TextView
        dialog_msg.text = message


        btncancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }
}