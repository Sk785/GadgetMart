package com.gadgetmart.ui.splash

import android.os.Handler
import android.view.WindowManager
import com.gadgetmart.R
import com.gadgetmart.app.Global
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.SplashActivityBinding
import com.gadgetmart.ui.home.HomeActivity
import com.gadgetmart.util.PreferenceManager
import io.branch.referral.Branch

class SplashActivity : BaseActivity<SplashActivityBinding>() {
    private var handler: Handler? = null
    private val timeDelay: Long = 3000
    lateinit var global:Global

    private val runnable: Runnable = Runnable {
        if (!isFinishing) {
            global.isLoading=true
            if (PreferenceManager().getInstance(this).getAuthToken().equals("")) {
                WelcomeActivity.start(this)
            }else {
                if (intent.data != null) {
                    Branch.getInstance()
                        .initSession({ referringParams, error ->
                            global.productID = referringParams.getString("productId");
                            global.varitionID = referringParams.getString("variationId");

                            HomeActivity.start(this)


                        }, intent.data, this@SplashActivity)
                }else{
                    HomeActivity.start(this)

                }
            }
            finish()
        }
    }

    override fun getContentView(): Int = R.layout.splash_activity

    override fun init(binding: SplashActivityBinding) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        handler = Handler()
        handler!!.postDelayed(runnable, timeDelay)
        global=applicationContext as Global
    }

    override fun onDestroy() {
        if (handler != null) handler!!.removeCallbacks(runnable)
        super.onDestroy()
    }
}
