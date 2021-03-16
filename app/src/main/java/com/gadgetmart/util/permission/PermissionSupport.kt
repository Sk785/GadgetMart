package com.gadgetmart.util.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.Uri.fromParts
import android.os.Build
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import com.gadgetmart.R
import com.gadgetmart.util.custom.StyledAlertDialog


class PermissionSupport {

    private val TAG = "PermissionSupport"

    private val REQUEST_PERMISSIONS_NORMAL = 10101
    private val REQUEST_PERMISSIONS_OVERLAY = 10102

    private var context: Context? = null
    private var activity: Activity? = null
    private var fragment: Fragment? = null
    private var callback: Callback? = null
    private var memory: PermissionMemory? = null

    private var who: Int = 0

    class Builder {

        private var activity: Activity? = null
        private var fragment: Fragment? = null
        private var callback: Callback? = null

        fun setActivity(activity: Activity): Builder {
            this.activity = activity
            return this
        }

        fun setFragment(fragment: Fragment): Builder {
            this.fragment = fragment
            return this
        }

        fun setCallback(callback: Callback): Builder {
            this.callback = callback
            return this
        }

        fun build(): PermissionSupport? {

            val permissionSupport = PermissionSupport()

            if (activity != null) {
                permissionSupport.withActivity(activity!!)
            } else if (fragment != null) {
                permissionSupport.withFragment(fragment!!)
            } else {
                return null
            }

            permissionSupport.withCallback(callback)
            return permissionSupport
        }
    }

    private fun withActivity(activity: Activity): PermissionSupport {
        this.activity = activity
        this.context = activity
        this.memory = PermissionMemory(context!!)
        return this
    }

    private fun withFragment(fragment: Fragment): PermissionSupport {
        this.fragment = fragment
        this.context = fragment.context
        this.memory = PermissionMemory(context!!)
        return this
    }

    private fun withCallback(callback: Callback?): PermissionSupport {
        this.callback = callback
        return this
    }

    private fun hasCallback(): Boolean {
        Log.d(TAG, "hasCallback() callback=" + callback!!)

        return callback != null
    }

    fun hasPermission(@NonNull permission: String): Boolean {

        if (context == null) {
            Log.d(TAG, "hasPermission() context=null.")
            return false
        }

        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || context!!.checkSelfPermission(
            permission
        ) === PackageManager.PERMISSION_GRANTED
    }

    fun hasOverlayPermission(): Boolean {

        if (context == null) {
            Log.d(TAG, "hasOverlayPermission() context=null.")
            return false
        }

        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context)
    }

    fun checkAndRequestPermissions(
        permissions: Array<String>,
        shouldCheckOverlayPermission: Boolean, who: Int
    ) {
        Log.d(TAG, "checkAndRequestPermissions()")
        this.who = who

        if (!hasCallback()) {
            Log.d(TAG, "checkAndRequestPermissions() callback=null.")
            return
        }

        val permissionStatusMap = HashMap<String, Boolean>()
        for (permission in permissions) {

            permissionStatusMap.put(permission, hasPermission(permission))
        }

        val hasNormalPermissions = !permissionStatusMap.containsValue(false)
        if (hasNormalPermissions) {

            if (!shouldCheckOverlayPermission) {
                callback!!.onPermissionsAllowed(who)
                return
            }

            if (hasOverlayPermission()) {
                callback!!.onPermissionsAllowed(who)
            } else {
                showOverlayPermissionRequestDialog()
            }

        } else {
            tryToRequestPermissions(permissions, REQUEST_PERMISSIONS_NORMAL)
        }
    }

    private fun tryToRequestPermissions(@NonNull permissions: Array<String>, requestCode: Int) {
        Log.d(TAG, "tryToRequestPermissions()")

        val whichActivity = if (activity != null)
            activity
        else
            if (fragment != null) fragment!!.activity else null

        if (whichActivity != null) {

            for (permission in permissions) {

                if (wasDeniedAtLeastOnce(permission)) {

                    val canAskPermission = ActivityCompat.shouldShowRequestPermissionRationale(
                        whichActivity, permission
                    )

                    if (!canAskPermission) {
                        showPermissionSettingsDialog()
                        return
                    }
                }
            }
        } else {
            showPermissionSettingsDialog()
            return
        }

        if (activity != null) {
            requestPermissions(activity!!, permissions, requestCode)
        } else if (fragment != null) {
            fragment!!.requestPermissions(permissions, requestCode)
        } else {

            // unable to request permissions
            showPermissionSettingsDialog()
        }
    }

    private fun showOverlayPermissionRequestDialog() {
        Log.d(TAG, "showOverlayPermissionRequestDialog()")

        if (context == null) {
            Log.d(TAG, "showOverlayPermissionRequestDialog() context=null")
            return
        }

        StyledAlertDialog.builder(context!!)
            .setCancelable(false)
            .setMessage(context!!.getString(R.string.overlay_perm_msg))
            .setPositiveButton(context!!.getString(R.string.settings)) { dialog, which ->

                val whichActivity = if (activity != null)
                    activity
                else
                    if (fragment != null) fragment!!.activity else null
                if (whichActivity != null) {

                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                    intent.data = Uri.parse("package:" + whichActivity.packageName)
                    whichActivity.startActivityForResult(intent, REQUEST_PERMISSIONS_OVERLAY)
                } else {
                    Log.d(TAG, "couldn't show overlay_permission settings.")
                }

                dialog.dismiss()
            }.show()
    }

    private fun showPermissionSettingsDialog() {
        Log.d(TAG, "showPermissionSettingsDialog()")

        if (context == null) {
            Log.d(TAG, "showPermissionSettingsDialog() context=null")
            return
        }

        StyledAlertDialog.builder(context!!)
            .setTitle(context!!.getString(R.string.permissions_required))
            .setMessage(context!!.getString(R.string.permissions_required_msg))
            .setPositiveButton(context!!.getString(R.string.alright)) { dialog, which ->

                if (context != null) {

                    val intent = Intent()
                    intent.action = ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.data = fromParts("package", context!!.packageName, null)
                    context!!.startActivity(intent)
                } else {
                    Log.d(TAG, "couldn't show permission settings.")
                }
            }.show()
    }

    fun consumePermissionResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ): Boolean {
        Log.d(TAG, "consumePermissionResult()")

        if (!hasCallback()) {
            Log.d(TAG, "consumePermissionResult() callback=null")
            return false
        }

        if (requestCode == REQUEST_PERMISSIONS_NORMAL) {
            val permissionStatusMap = HashMap<String, Boolean>()
            if (permissions.isNotEmpty() && permissions.size == grantResults.size) {

                for (i in permissions.indices) {

                    permissionStatusMap.put(
                        permissions[i],
                        grantResults[i] == PackageManager.PERMISSION_GRANTED
                    )
                }
            }
            if (permissionStatusMap.containsValue(false)) {

                for (entry in permissionStatusMap.entries) {

                    if (!entry.value) {
                        // this permission was denied
                        setPermissionDeniedAtLeastOnce(entry.key)
                    }
                }

                callback!!.onPermissionsNotAllowed()
            } else {
                callback!!.onPermissionsAllowed(who)
            }
            return true
        }

        return false
    }

    private fun wasDeniedAtLeastOnce(@NonNull permission: String): Boolean {
        Log.d(TAG, "wasDeniedAtLeastOnce() memory=" + memory!!)

        return memory != null && memory!!.getPermissionPref(permission)
    }

    private fun setPermissionDeniedAtLeastOnce(@NonNull permission: String) {
        Log.d(TAG, "setPermissionDeniedAtLeastOnce() memory=" + memory!!)

        if (memory != null) {
            memory!!.setPermissionPref(permission, true)
        }
    }

    interface Callback {

        fun onPermissionsAllowed(who: Int)

        fun onPermissionsNotAllowed()
    }
}