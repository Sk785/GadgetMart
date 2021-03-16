package com.gadgetmart.base

import android.Manifest
import android.app.AlertDialog
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.facebook.FacebookSdk
import com.gadgetmart.R
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.permission.PermissionSupport


/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * the base methods to be used by all activities, e.g creation of activity, show some toast, etc
 */

public abstract class BaseActivity<BINDING : ViewDataBinding> : AppCompatActivity(),
    BaseActivityExtension, PermissionSupport.Callback {

    private lateinit var binding: BINDING
    private var permissionSupport: PermissionSupport? = null
    val RC_ACCESS_LOCATION_PERM = 10006
    private val TAG = BaseActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionSupport = getPermissionSupport()
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        FacebookSdk.sdkInitialize(this)
        binding = DataBindingUtil.setContentView(this, getContentView())
        init(binding)
    }

    abstract fun getContentView(): Int

    abstract fun init(binding: BINDING)

    fun binding(): BINDING {
        return binding
    }

    override fun replaceFragment(
        fragment: BaseFragment<*>,
        tag: String,
        addToBackStack: Boolean,
        shouldAnimateTransition: Boolean
    ) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(tag)
        }

        fragmentTransaction.replace(R.id.fragment_container, fragment, tag)
        fragmentTransaction.commitAllowingStateLoss()
    }


    private fun showShortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun hasNetwork(): Boolean {
        return ContextUtils.hasNetwork(this)
    }

    override fun clearUser() {
        //Clear Preference manager data, if stored
    }

    override fun showToast(message: String) {
        showShortToast(message)
    }

    fun showToast(stringResourceId: Int) {
        showToast(getString(stringResourceId))
    }

    fun makeLocationPermissionRequest() {
        permissionSupport!!.checkAndRequestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), false, RC_ACCESS_LOCATION_PERM
        )
    }

    fun getPermissionSupport(): PermissionSupport {

        if (permissionSupport == null) {
            permissionSupport = PermissionSupport.Builder()
                .setActivity(this)
                .setCallback(this)
                .build()
        }

        return permissionSupport!!
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {

        val isConsumed = getPermissionSupport()
            .consumePermissionResult(requestCode, permissions, grantResults)
        if (!isConsumed) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onPermissionsAllowed(who: Int) {
        Log.d(TAG, "This call to onPermissionsAllowed() was consumed by BaseActivity.")
    }

    override fun onPermissionsNotAllowed() {
        Log.d(TAG, "This call to onPermissionsNotAllowed() was consumed by BaseActivity.")
    }

    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
            return
        }

        finish()
    }

    override fun openInternetDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Internet Alert!")
        alertDialogBuilder
            .setMessage("You are not connected to Internet..Please Enable Internet!")
            .setCancelable(false)

            .setPositiveButton(
                "Ok"
            ) { dialog, id ->
                dialog.dismiss()

                val intent = Intent(Intent.ACTION_MAIN, null)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                val cn = ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings")
                intent.component = cn
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id: Int) {
                    dialog.dismiss()
                }
            })
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()


    }


}