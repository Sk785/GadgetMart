package com.gadgetmart.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.HomeActivityBinding
import com.gadgetmart.databinding.HomeNavDrawerHeaderBinding
import com.gadgetmart.ui.auth._common.AuthResult
import com.gadgetmart.ui.cart_bag.CartActivity
import com.gadgetmart.ui.dashboard.DashboardFragment
import com.gadgetmart.ui.home.model.SettingModel
import com.gadgetmart.ui.my_account.MyAccountActivity
import com.gadgetmart.ui.my_account.ProfileContract
import com.gadgetmart.ui.my_account.ProfilePresenter
import com.gadgetmart.ui.order.MyOrder
import com.gadgetmart.ui.search.SearchActivity
import com.gadgetmart.ui.splash.WelcomeActivity
import com.gadgetmart.ui.support.SupportCenterActivity
import com.gadgetmart.ui.support.TermsAndCondtions
import com.gadgetmart.ui.support.WebActivity
import com.gadgetmart.ui.wishlist.MyWishListActivity
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.CustomTypefaceSpan
import com.gadgetmart.util.PreferenceManager
import com.github.javiersantos.appupdater.AppUpdater
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.layout_toolbar_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : BaseActivity<HomeActivityBinding>(), ProfileContract {
    private var binding: HomeActivityBinding? = null
    private lateinit var headerBinding: HomeNavDrawerHeaderBinding
    private var actionbarToggle: ActionBarDrawerToggle? = null
    private var profilePresenter: ProfilePresenter? = null

    /*  private var mMenuAdapter: MenuAdapter? = null
      private var mViewHolder: ViewHolder? = null*/
    var seetingModel: SettingModel? = null

    override fun getContentView(): Int {
        return R.layout.home_activity
    }

    @SuppressLint("WrongConstant")
    override fun init(binding: HomeActivityBinding) {

        this.binding = binding
        replaceFragment(
            DashboardFragment().newInstance(), "DashboardFragment",
            addToBackStack = true,
            shouldAnimateTransition = true
        )

        setSupportActionBar(binding.toolbar)

        //        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        drawerLayout.setScrimColor(Color.TRANSPARENT);
        val actionbarToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        actionbarToggle.syncState()

        binding.drawerLayout.setViewScale(Gravity.START, 0.9f)
        binding.drawerLayout.setViewElevation(Gravity.START, 20f)


        profilePresenter =
            ProfilePresenter(this, this)

        ContextUtils.getAuthToken(this).let {
            profilePresenter!!.fetchUserProfile(
                it
            )
        }
        headerBinding = HomeNavDrawerHeaderBinding.inflate(
            LayoutInflater.from(binding.navView.context)
        )

        binding.navView.addHeaderView(headerBinding.root)
        val m: Menu = binding.navView.menu
        for (i in 0 until m.size()) {
            val mi = m.getItem(i)

            applyFontToMenuItem(mi)

        }
        val appUpdater = AppUpdater(this)
        appUpdater.start()

        getUserPreferences()

        initListeners(binding)
        getSettings()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getUserPreferences()
        replaceFragment(
            DashboardFragment().newInstance(), "DashboardFragment",
            addToBackStack = true,
            shouldAnimateTransition = true
        )
    }
    private fun getUserPreferences() {
        if (PreferenceManager().getInstance(this)
                .getProfileImage() != null && PreferenceManager().getInstance(
                this
            ).getProfileImage()!!.isNotEmpty()
        ) {
            Glide.with(this)
                .load(PreferenceManager().getInstance(this).getProfileImage())
                .circleCrop()

                .into(headerBinding.profilePicImageView)
        }/*else{
            val drawable2 = TextDrawable.builder()
                .buildRound(PreferenceManager().getInstance(this).getUserName()!![0].toString(), resources.getColor(R.color.colorPrimary))
            headerBinding.profilePicImageView.setImageDrawable(drawable2)
        }*/

        if (!PreferenceManager().getInstance(this).getUserName().isNullOrEmpty()) {
            headerBinding.userNameTextView.text =
                PreferenceManager().getInstance(this).getUserName()
        }

        if (!PreferenceManager().getInstance(this).getUserEmail().isNullOrEmpty()) {
            headerBinding.userEmailIdTextView.text =
                PreferenceManager().getInstance(this).getUserEmail()
        }
    }

    private fun updateDrawerMenu(binding: HomeActivityBinding) {
        // get menu from navigationView
        val menu: Menu = binding.navView.menu

        // find MenuItem you want to change
        val navLogOut: MenuItem = menu.findItem(R.id.nav_menu_log_out)

        // set new title to the MenuItem
        navLogOut.title = "Login"

    }

    private fun initListeners(binding: HomeActivityBinding) {

        nav_drawer_icon.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
            }
        }
        headerBinding.profileView.setOnClickListener{
            if(binding.navView.menu.findItem(R.id.nav_menu_log_out).title.equals("Login")){
            }else {
                MyAccountActivity.start(this)
                closeDrawerIfOpened()
            }
        }
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor =
                        ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                }
            }

            override fun onDrawerClosed(drawerView: View) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = ContextCompat.getColor(this@HomeActivity, R.color.white)
                }
            }

            override fun onDrawerOpened(drawerView: View) {

            }
        })
        toolbar_ohnik_image_view.setOnClickListener {
            val intent = Intent(applicationContext, SearchActivity::class.java)

            startActivity(intent)
        }

        toolbar_cart_icon.setOnClickListener {
            val intent = Intent(applicationContext, CartActivity::class.java)

            startActivity(intent)
        }
//        auth_bottom_view.animate().translationY(0F)
if(PreferenceManager().getInstance(this).getAuthToken().equals("")){
    binding.navView.menu.findItem(R.id.nav_menu_log_out).title = "Login"

}else{
    binding.navView.menu.findItem(R.id.nav_menu_log_out).title = "Logout"

}

        binding.navView.setNavigationItemSelectedListener {
            return@setNavigationItemSelectedListener when (it.itemId) {

                R.id.nav_menu_dashboard -> {
                    closeDrawerIfOpened()
                    true
                }

                R.id.nav_menu_my_account -> {
                    if(binding.navView.menu.findItem(R.id.nav_menu_log_out).title.equals("Login")){
                        loginUser(binding)
                        closeDrawerIfOpened()

                    }else {
                        MyAccountActivity.start(this)
                        closeDrawerIfOpened()
                    }

                    true
                }

                R.id.nav_menu_my_orders -> {
                    val i = Intent(this@HomeActivity, MyOrder::class.java)
                    startActivity(i)
                    closeDrawerIfOpened()
                    true
                }

                R.id.nav_menu_my_wish_list -> {
                    MyWishListActivity.start(this)
                    closeDrawerIfOpened()
                    true
                }

                R.id.nav_menu_gadget_mart_term -> {
                    closeDrawerIfOpened()
//                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gadgetmart.in/public/getGeneralInfo/3"))
//                    startActivity(browserIntent)
                    val i = Intent(applicationContext, WebActivity::class.java)
                    i.putExtra("type", resources.getString(R.string.title_menu_gadget_mart_term))
                    i.putExtra("data", "https://gadgetmart.in/public/getGeneralInfo/3")

                    startActivity(i)
                    true
                }
                R.id.nav_menu_gadget_mart_privacy -> {
                    closeDrawerIfOpened()
//                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gadgetmart.in/public/getGeneralInfo/2"))
//                    startActivity(browserIntent)
                    val i = Intent(applicationContext, WebActivity::class.java)
                    i.putExtra("type", resources.getString(R.string.title_menu_gadget_mart_privecy))
                    i.putExtra("data", "https://gadgetmart.in/public/getGeneralInfo/2")

                    startActivity(i)
                    true
                }
                R.id.nav_menu_gadget_mart_refund -> {
                    closeDrawerIfOpened()
//                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gadgetmart.in/public/getGeneralInfo/1"))
//                    startActivity(browserIntent)
                    val i = Intent(applicationContext, WebActivity::class.java)
                    i.putExtra("type", resources.getString(R.string.title_menu_gadget_mart_refund))
                    i.putExtra("data", "https://gadgetmart.in/public/getGeneralInfo/1")

                    startActivity(i)
                    true
                }

                R.id.nav_menu_log_out -> {
                    if(binding.navView.menu.findItem(R.id.nav_menu_log_out).title.equals("Login")){
                        loginUser(binding)
                    }else {
                        logoutUser(binding)
                    }
                    true
                }
                R.id.nav_menu_gadget_mart_support_care -> {
                    closeDrawerIfOpened()
                    val i = Intent(applicationContext, SupportCenterActivity::class.java)
                    i.putExtra("whtsup", seetingModel!!.data?.support_centre?.whatsapp?.get(0))
                    i.putExtra("phone", seetingModel!!.data?.support_centre?.mobile?.get(0))
                    i.putExtra("email", seetingModel!!.data?.support_centre?.email?.get(0))

                    startActivity(i)

                    true
                }

                else -> {
                    replaceFragment(
                        DashboardFragment().newInstance(), "DashboardFragment",
                        addToBackStack = true,
                        shouldAnimateTransition = true
                    )
                    closeDrawerIfOpened()
                    true
                }
            }
        }
    }

    private fun logoutUser(binding: HomeActivityBinding) {

        closeDrawerIfOpened()

        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.custom_alert_dialog)
        if (dialog.window != null) {
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        }
        val btnok = dialog.findViewById(R.id.okbtn) as TextView
        val btncancel = dialog.findViewById(R.id.cancelbtn) as TextView

        btnok.setOnClickListener {
            dialog.dismiss()

            if (PreferenceManager().getInstance(this).getGoogleTOken().equals("")) {

            } else {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()

                val googleSignInClient = GoogleSignIn.getClient(this@HomeActivity, gso)
                googleSignInClient.signOut()
                LoginManager.getInstance().logOut()

            }
            PreferenceManager().getInstance(this).clearUserData()
            updateDrawerMenu(binding)
            WelcomeActivity.start(this@HomeActivity)
            // val intent = Intent(applicationContext, WelcomeActivity::class.java)
            /*intent.putExtra(Constants.email, "")
            intent.putExtra(Constants.socialId, "")
            intent.putExtra(Constants.type, "")
            startActivity(intent)*/
            finishAffinity()
        }

        btncancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun closeDrawerIfOpened() {
        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            }
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context!!.startActivity(intent)
        }

    }

    override fun onBackPressed() {
        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            }
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        if (PreferenceManager().getInstance(this).getProfileImage()!!.isNotEmpty()) {
            Glide.with(this)
                .load(PreferenceManager().getInstance(this).getProfileImage())
                .circleCrop()

                .into(headerBinding.profilePicImageView)
        }
    }

    override fun onProfileFound(authResult: AuthResult?, message: String) {

        PreferenceManager().getInstance(this).setUserCountry(authResult?.getCountry())
        PreferenceManager().getInstance(this).setUserCountryCode(authResult?.getCountryCode())
        PreferenceManager().getInstance(this).setUserGender(authResult?.getGender())
        PreferenceManager().getInstance(this).setUserPhone(authResult?.getPhone())
        PreferenceManager().getInstance(this).setUserName(authResult?.getName())
        PreferenceManager().getInstance(this).setUserEmail(authResult?.getEmail())
        PreferenceManager().getInstance(this).setProfileImage(authResult?.getPhoto())

        if (PreferenceManager().getInstance(this).getProfileImage()!!.isNotEmpty()) {
            Glide.with(this)
                .load(PreferenceManager().getInstance(this).getProfileImage())
                .into(headerBinding.profilePicImageView)
        }
    }

    override fun onProfileNotFound(message: String) {

    }

    private fun applyFontToMenuItem(mi: MenuItem) {
        val font = Typeface.createFromAsset(assets, "montserrat_medium.ttf")
        val mNewTitle = SpannableString(mi.title)
        mNewTitle.setSpan(
            CustomTypefaceSpan("", font),
            0,
            mNewTitle.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        //mNewTitle.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, mNewTitle.length(), 0); Use this if you want to center the items
        mi.title = mNewTitle
    }

    private fun getSettings() {
        try {

            ApiClientGenerator
                .getClient()!!
                .getSettings()
                .enqueue(object : Callback<SettingModel?> {
                    override fun onResponse(
                        call: Call<SettingModel?>,
                        response: Response<SettingModel?>
                    ) {
                        if (response.body()!!.status == 1) {
                            seetingModel = response.body()
                        } else {

                        }
                    }

                    override fun onFailure(
                        call: Call<SettingModel?>,
                        t: Throwable
                    ) {

                        //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loginUser(binding: HomeActivityBinding) {

        closeDrawerIfOpened()

        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.login_message_dialog)
        if (dialog.window != null) {
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        }
        val btnok = dialog.findViewById(R.id.okbtn) as TextView
        val btncancel = dialog.findViewById(R.id.cancelbtn) as TextView
        val dialog_msg=dialog.findViewById(R.id.dialog_msg) as TextView
        dialog_msg.text="Please login to continue"

        btnok.setOnClickListener {
            dialog.dismiss()

            if (PreferenceManager().getInstance(this).getGoogleTOken().equals("")) {

            } else {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()

                val googleSignInClient = GoogleSignIn.getClient(this@HomeActivity, gso)
                googleSignInClient.signOut()
                LoginManager.getInstance().logOut()

            }
            updateDrawerMenu(binding)
            PreferenceManager().getInstance(this).clearUserData()

            WelcomeActivity.start(this@HomeActivity)
            // val intent = Intent(applicationContext, WelcomeActivity::class.java)
            /*intent.putExtra(Constants.email, "")
            intent.putExtra(Constants.socialId, "")
            intent.putExtra(Constants.type, "")
            startActivity(intent)*/
            finishAffinity()
        }

        btncancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

}
