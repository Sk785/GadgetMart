package com.gadgetmart.ui.my_account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.gadgetmart.BuildConfig
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.MyAccountActivityBinding
import com.gadgetmart.ui.auth.RegisterSetupActivity
import com.gadgetmart.ui.auth._common.AuthResult
import com.gadgetmart.ui.auth.otp_verification.OtpVerificationFragment
import com.gadgetmart.ui.edit_profile.EditProfileActivity
import com.gadgetmart.ui.my_address.MyAddressesActivity
import com.gadgetmart.ui.my_review.MyReviewActivity
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.Constants
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.PreferenceManager
import com.gadgetmart.util.custom.CustomProgressDialog
import kotlinx.android.synthetic.main.welcome_activity.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyAccountActivity : BaseActivity<MyAccountActivityBinding>(), ProfileContract {

    private var profilePresenter: ProfilePresenter? = null
    lateinit var binding: MyAccountActivityBinding
    private var customProgressDialog: CustomProgressDialog? = null
    var isNotifications=false

    var versionName=""
    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, MyAccountActivity::class.java)
            context!!.startActivity(intent)
        }

    }

    override fun getContentView(): Int {
        window.statusBarColor = ContextCompat.getColor(this, R.color.red_light)
        return R.layout.my_account_activity
    }

    override fun init(binding: MyAccountActivityBinding) {
        this.binding = binding
        binding.toolbarId.toolbarBackIcon?.setOnClickListener { finish() }
        customProgressDialog = CustomProgressDialog(this)
         versionName = BuildConfig.VERSION_NAME


binding.versionName.setText("Version:-" +versionName)
        binding.toolbarId.toolbarParentLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        binding.toolbarId.toolbarTitleTextView?.text = "My Account"
        binding.userNameTextView.text = PreferenceManager().getInstance(this).getUserName()
        binding.userEmailIdTextView.text = PreferenceManager().getInstance(this).getUserEmail()
        if (PreferenceManager().getInstance(this).getProfileImage() != null && PreferenceManager().getInstance(this).getProfileImage()!!.isNotEmpty()){
            Glide.with(this)
                .load(PreferenceManager().getInstance(this).getProfileImage())
                .circleCrop().placeholder(R.drawable.dp_palceholder)

                .into(binding.profilePicImageView)
        }/*else{
            val drawable2 = TextDrawable.builder()
                .buildRound(PreferenceManager().getInstance(this).getUserName()!![0].toString(), resources.getColor(R.color.colorPrimary))
            binding.profilePicImageView.setImageDrawable(drawable2)
        }*/

        initListeners(binding)

    }

    fun initListeners(binding: MyAccountActivityBinding) {
        Log.e("status33333333",PreferenceManager().getInstance(this).getNotifications())
        if(PreferenceManager().getInstance(this).getNotifications().equals("No")){
            isNotifications=false
        }else{
            isNotifications=true

        }
        binding.switcher.setChecked(isNotifications)
        binding.notificationsTextView.setOnClickListener { onNotificationsSettingsTapped() }

        binding.myAddressesTextView.setOnClickListener { onMyAddressesTapped() }

        binding.myReviewsTextView.setOnClickListener { onMyReviewsTapped() }

        binding.continueButton.setOnClickListener { onEditProfileTapped() }
        binding.switcher.setOnCheckedChangeListener { checked ->
            customProgressDialog!!.dialogShow()
            changeNotificationStatus()
        }


    }

    private fun onNotificationsSettingsTapped() {


    }

    private fun onMyAddressesTapped() {
        val intent = Intent(applicationContext, MyAddressesActivity::class.java)
        intent.putExtra("type","account")
        startActivity(intent)
    }

    private fun onMyReviewsTapped() {
        val intent = Intent(applicationContext, MyReviewActivity::class.java)
        startActivity(intent)

    }

    private fun onEditProfileTapped(){
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivityForResult(intent,101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101){
            binding.userNameTextView.text = PreferenceManager().getInstance(this).getUserName()
            binding.userEmailIdTextView.text = PreferenceManager().getInstance(this).getUserEmail()
            if (PreferenceManager().getInstance(this).getProfileImage() != null && PreferenceManager().getInstance(this).getProfileImage()!!.isNotEmpty()){
                Glide.with(this)
                    .load(PreferenceManager().getInstance(this).getProfileImage())
                    .circleCrop()
                    .into(binding.profilePicImageView)
            }
        }
    }
    override fun onProfileFound(authResult: AuthResult?, message: String) {

        binding().userNameTextView.text = authResult?.getName()
        binding().userEmailIdTextView.text = authResult?.getEmail()
        PreferenceManager().getInstance(this).setUserCountry(authResult?.getCountry())
        PreferenceManager().getInstance(this).setUserCountryCode(authResult?.getCountryCode())
        PreferenceManager().getInstance(this).setUserGender(authResult?.getGender())
        PreferenceManager().getInstance(this).setUserPhone(authResult?.getPhone())
        PreferenceManager().getInstance(this).setProfileImage(authResult?.getPhoto())

    }

    override fun onProfileNotFound(message: String) {
        AppUtil.firebaseEvent(applicationContext,"error","error_events",message
        )
        Log.e("Error ", "" + message)
     }

    override fun onResume() {
        super.onResume()
        if (PreferenceManager().getInstance(this).getProfileImage()!!.isNotEmpty()){
            Glide.with(this)
                .load(PreferenceManager().getInstance(this).getProfileImage())
                .circleCrop()

                .into(binding.profilePicImageView)
        }
       /* if (profilePresenter != null) {
            profilePresenter =
                ProfilePresenter(this, this)

            ContextUtils.getAuthToken(this).let {
                profilePresenter!!.fetchUserProfile(
                    it
                )
            }
        }*/
    }

    //-------------------------Send otp----------------
    private fun changeNotificationStatus() {
        try {

            ApiClientGenerator
                .getClient()!!
                .changeNotificationStatus(
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
                        Log.e("response",response.toString())
                        if (response.getString("status") == "1") {

                            if(isNotifications==false){
    isNotifications=true
    PreferenceManager().getInstance(applicationContext).setNotifications("Yes")                     // finish()

}else{
    isNotifications=false
    PreferenceManager().getInstance(applicationContext).setNotifications("No")                     // finish()


}


                        } else {
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
}


