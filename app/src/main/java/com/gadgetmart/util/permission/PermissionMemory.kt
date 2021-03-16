package com.gadgetmart.util.permission

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.gadgetmart.BuildConfig


class PermissionMemory(context: Context) {

    private val PERMISSION_PREF_NAME = BuildConfig.APPLICATION_ID + ".perm"

    private var mPermissionPref: SharedPreferences? = null

    init {
        this.mPermissionPref = context
            .applicationContext
            .getSharedPreferences(PERMISSION_PREF_NAME, Context.MODE_PRIVATE)
    }

    /*
     * Permission Pref
     */

    fun setPermissionPref(permission: String, value: Boolean) {

        Log.d("", "setPermissionPref() was_denied_$permission=$value")
        mPermissionPref!!.edit().putBoolean("was_denied_$permission", value).apply()
    }

    fun getPermissionPref(permission: String): Boolean {

        val value = mPermissionPref!!.getBoolean("was_denied_$permission", false)
        Log.d("","getPermissionPref() was_denied_$permission=$value")
        return value
    }
}