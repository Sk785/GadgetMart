package com.gadgetmart.ui.splash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.WelcomeActivityBinding
import com.gadgetmart.ui.auth.RegisterSetupActivity
import com.gadgetmart.ui.auth._common.AuthResult
import com.gadgetmart.ui.home.HomeActivity
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.Constants
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.PreferenceManager
import com.gadgetmart.util.custom.CustomProgressDialog
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.welcome_activity.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.MalformedURLException
import java.net.URL
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class WelcomeActivity : BaseActivity<WelcomeActivityBinding>(),
    GoogleApiClient.OnConnectionFailedListener {

    private var googleSignInClient: GoogleSignInClient? = null
    private lateinit var fireBaseAuth: FirebaseAuth
    private var myDialog: CustomProgressDialog? = null

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, WelcomeActivity::class.java)
            context!!.startActivity(intent)
        }

    }

    var socialType: String = "";

    //------------facebook variable---------
    lateinit var mCallbackManager: CallbackManager

    //-------------google variable--------------
    private val RC_SIGN_IN = 9001
    lateinit var mGoogleApiClient: GoogleApiClient
    var name = ""
    var email = ""
    var UserPic = ""
    var socialId = ""
    var id = ""
    var mProfilePic = ""


    override fun init(binding: WelcomeActivityBinding) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        myDialog = CustomProgressDialog(this)
        initListeners(binding)
        mCallbackManager = CallbackManager.Factory.create()
//        signInWithGoogle()

        // hashkey()
    }

    private fun initListeners(binding: WelcomeActivityBinding) {
        binding.getStartedButton.setOnClickListener {
            email = ""
            socialId = ""
            socialType = ""
            val intent = Intent(applicationContext, RegisterSetupActivity::class.java)
            intent.putExtra("name", "")
            intent.putExtra("type1", "0")


            intent.putExtra(Constants.email, email)
            intent.putExtra(Constants.socialId, socialId)
            intent.putExtra(Constants.type, socialType)
            intent.putExtra("isLogin", "yes")

            startActivity(intent)
        }

        binding.rlFbLogin.setOnClickListener {
            if (hasNetwork()){
                button_Login.performClick()
                facebookLogin()
            }
            else openInternetDialog()
        }

        binding.rlGoogleLogin.setOnClickListener {
            if (hasNetwork()) onLoginWithGoogleButtonTapped()
            else openInternetDialog()
        }
        binding.continueAsGuest.setOnClickListener {
            AppUtil.firebaseEvent(applicationContext,"","guest_user","")

            HomeActivity.start(this)
            finishAffinity()
        }
    }

    override fun getContentView(): Int {
        return R.layout.welcome_activity
    }

    fun hashkey() {
        try {
            val info =
                packageManager.getPackageInfo("com.gadgetmart", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val sign = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                //textInstructionsOrLink = (TextView)findViewById(R.id.textstring);
                //textInstructionsOrLink.setText(sign);
                Log.e("Hash Key", sign)
                Toast.makeText(applicationContext, sign, Toast.LENGTH_LONG).show()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("nope", "nope")
        } catch (e: NoSuchAlgorithmException) {
        }

    }

    /**
     * Facebook Integration
     */
    private fun onLoginWithFbButtonTapped() {
        if (!ContextUtils.hasNetwork(this)) {
            showToast(R.string.error_internet_connection_availability)
            return
        }
        myDialog?.dialogShow()

// If the access token is available already assign it.


        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf("email", "public_profile"))

        LoginManager.getInstance()
            .registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {

                override fun onSuccess(loginResult: LoginResult) {
                    Log.e("Success ", " Login successful +++  ")

                    if (AccessToken.getCurrentAccessToken() != null) {
                        myDialog?.dialogDismiss()
                        requestData(AccessToken.getCurrentAccessToken(), loginResult)
                    }
                }

                override fun onCancel() {
                    myDialog?.dialogDismiss()
                    Log.e("Cancel ", " Login Cancel +++  ")
                }

                override fun onError(error: FacebookException) {
                    myDialog?.dialogDismiss()
                    Log.e("Error ", " Login error +++  ")
                    //                NetworkUtil.openInternetDialog(LoginActivity.this);
                }
            })
    }

    fun requestData(accessToken: AccessToken?, loginResult: LoginResult) {
        val request = GraphRequest.newMeRequest(
            if (accessToken != null && !accessToken.isExpired) accessToken
            else loginResult.accessToken
        ) { `object`, _ ->
            Log.e("json", `object`.toString())

            try {
                if (`object`.has("email")) {
                    email = `object`.getString("email")
                }

                if (`object`.has("name")) {
                    name = `object`.getString("name")
                }

                val accessToken = loginResult.accessToken.token

                socialId = if (`object`.has("id")) {
                    `object`.getString("id")
                } else {
                    ""
                }
                if (socialId != "") {
                    val profileImage = "https://graph.facebook.com/$socialId/picture?type=normal"
                    UserPic = profileImage
                }

                LoginManager.getInstance().logOut()
                if (accessToken.isNullOrEmpty())
                    showToast("Invalid account!! Please try to login again.")
                else {
                    socialType = "facebook"

                    socialLoginStatus(
                        WelcomeRequest()
                            .setFacebookToken(accessToken)
                            .setEmail(email)
                            .setName(name)
                            .setDeviceToken(PreferenceManager(this).getOneSignalNotificationId())
                            .setPhoto(UserPic)
                    )
                }

            } catch (e: JSONException) {
                myDialog?.dialogDismiss()
                Log.e("Error", "" + e.message)
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "id,name,email")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun facebookLogin() {
        button_Login.setReadPermissions("email")
        button_Login.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val accessToken = loginResult.accessToken.token
                Log.i("accessToken", accessToken)
                val graphRequest = GraphRequest.newMeRequest(
                    loginResult.accessToken
                ) { `object`, response ->
                    Log.e("response", `object`.toString())
                    val mFacebookData = getFacebookData(`object`)

                    AccessToken.setCurrentAccessToken(null);
                    if (LoginManager.getInstance() != null) {
                        LoginManager.getInstance().logOut();
                    }
                    name = mFacebookData.getString("name")!!
                    email = mFacebookData.getString("email")!!
                    //String gender = mFacebookData.getString("gender");

                    UserPic = mFacebookData.getString("profile_pic")!!
                    socialId = mFacebookData.getString("id")!!
                    myDialog?.dialogShow()
                    socialType = "facebook"

                    socialLoginStatus(
                        WelcomeRequest()
                            .setFacebookToken(accessToken)
                            .setEmail(email)
                            .setName(name)
                            .setDeviceToken(PreferenceManager(this@WelcomeActivity).getOneSignalNotificationId())
                            .setPhoto(UserPic)
                    )

                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name, email,gender,picture.type(large),birthday")
                graphRequest.parameters = parameters
                graphRequest.executeAsync()

            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {

            }

        })
    }

    private fun getFacebookData(`object`: JSONObject): Bundle {
        val bundle = Bundle()
        try {
            id = `object`.getString("id")
            val pictureObj = `object`.getJSONObject("picture")
            val data = pictureObj.getJSONObject("data")
            mProfilePic = URL("https://graph.facebook.com/$id/picture?type=large").toString()


            bundle.putString("profile_pic", mProfilePic.toString())
            if (`object`.has("name")) {
                bundle.putString("name", `object`.getString("name"))
            }
            if (`object`.has("id")) {
                bundle.putString("id", `object`.getString("id"))
            }
            if (`object`.has("email")) {
                bundle.putString("email", `object`.getString("email"))
            }else{
                bundle.putString("email", "")

            }
            if (`object`.has("gender")) {

                bundle.putString("gender", `object`.getString("gender"))
            }else{
                bundle.putString("gender","")

            }

        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        return bundle
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        myDialog?.dialogDismiss()
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {
                Log.e("requestCode", "" + data)

//            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                val result = GoogleSignIn.getSignedInAccountFromIntent(data)
                result?.let { handleSignInResult(it) }
            } else {
                myDialog?.dialogDismiss()
                mCallbackManager.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    //--------------------google method-----------------

    /**
     * Google integration
     */
    private fun onLoginWithGoogleButtonTapped() {
        if (!ContextUtils.hasNetwork(this)) {
            showToast(R.string.error_internet_connection_availability)
            return
        }
        myDialog?.dialogShow()

        fireBaseAuth = FirebaseAuth.getInstance()

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient =
            applicationContext?.let { GoogleSignIn.getClient(it, googleSignInOptions) }

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null)
//            updateUI(account)
        else
            startActivityForResult(googleSignInClient?.signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(account: GoogleSignInAccount?) {
//        val registrationRequest = RegistrationRequest()
//        registrationRequest.emailId = account?.email
//        registrationRequest.userName = account?.displayName
//        registrationRequest.googleToken = account?.idToken
//        signInPresenter?.validateFields(registrationRequest)
    }

    private fun signInWithGoogle() {


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()


    }

    fun loginWithGoogle() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleSignInResult(result: Task<GoogleSignInAccount>) {
        val account: GoogleSignInAccount? = result.getResult(ApiException::class.java)
        myDialog?.dialogDismiss()

        if (account != null) {
            socialId = account.idToken.toString()
            name = account.displayName.toString()
            email = account.email.toString()
            socialType = "google";
            UserPic = account.photoUrl.toString()
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            if (account.idToken.isNullOrEmpty())
                showToast("Invalid account!! Please try to login again.")
            else {

                //Now using FireBase we are signing in the user here
                fireBaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("Login>>", "signInWithCredential:success")
                            val gso =
                                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .build()

                            val googleSignInClient = GoogleSignIn.getClient(this, gso)
                            googleSignInClient.signOut()


                            socialLoginStatus(
                                WelcomeRequest()
                                    .setGoogleToken(socialId)
                                    .setEmail(email)
                                    .setDeviceToken(PreferenceManager(this).getOneSignalNotificationId())
                                    .setName(name)
                                    .setPhoto(UserPic)
                            )
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login>>", "signInWithCredential:failure", task.exception)
                        }
                    }
            }
        }
    }


    private fun handleSignInResult(result: GoogleSignInResult) {
        Log.e("handleSignInResult:", "" + result.isSuccess())

        if (result.isSuccess()) {
            // Signed in successfolly, show authenticated UI.
            val acct = result.getSignInAccount()

            name = acct?.displayName.toString()
            email = acct?.email.toString()
            //UserPic = acct?.photoUrl!!.toString()
            socialId = acct?.getId().toString()
            socialType = "google";
            if (acct?.getPhotoUrl() != null) {
                UserPic = acct?.getPhotoUrl().toString()
            }
            socialLoginStatus(
                WelcomeRequest()
                    .setGoogleToken(socialId)
                    .setEmail(email)
                    .setDeviceToken(PreferenceManager(this).getOneSignalNotificationId())
                    .setName(name)
                    .setPhoto(UserPic)
            )


        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    private fun socialLoginStatus(jsonObject: WelcomeRequest) {

        if (TextUtils.isEmpty(jsonObject.getDeviceToken())) {
            showToast("Invalid device token")
            return
        }

        myDialog?.dialogShow()
        ApiClientGenerator
            .getClient()!!
            .socialLogin(jsonObject)
            .enqueue(object : Callback<ApiResponse<AuthResult>> {

                override fun onResponse(
                    call: Call<ApiResponse<AuthResult>>,
                    response: Response<ApiResponse<AuthResult>>
                ) {
                    myDialog?.dialogDismiss()

                    if (response.body()!!.status == 1) {
                        if(response.body()!!.message.contains("User Not Registered")){
                            val intent = Intent(applicationContext, RegisterSetupActivity::class.java)
                            intent.putExtra("name", name)

                            intent.putExtra("type1", "1")

                            intent.putExtra(Constants.email, email)
                            intent.putExtra(Constants.socialId, socialId)
                            intent.putExtra(Constants.type, socialType)
                            intent.putExtra("isLogin", "yes")

                            startActivity(intent)
                        }else{
                            PreferenceManager().getInstance(this@WelcomeActivity)
                                .saveSignInResult(response.body()!!.data)
                            response.body()?.message?.let { showToast(it) }
                            HomeActivity.start(applicationContext)
                            finish()
                        }

                    }  else {
                        if(response.body()!!.message.contains("User Not Registered")){
                            val intent = Intent(applicationContext, RegisterSetupActivity::class.java)
                            intent.putExtra("name", name)
                            intent.putExtra("type1", "1")

                            intent.putExtra(Constants.email, email)
                            intent.putExtra(Constants.socialId, socialId)
                            intent.putExtra(Constants.type, socialType)
                            intent.putExtra("isLogin", "yes")

                            startActivity(intent)
                        }else{
                            showToast(response.body()!!.message)
                        }

                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<AuthResult>>,
                    t: Throwable
                ) {
                    myDialog?.dialogDismiss()
                }
            })


    }


}