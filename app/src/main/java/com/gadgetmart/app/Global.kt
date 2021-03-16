package com.gadgetmart.app

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.gadgetmart.ui.CustomNotifications.NotificationUtils
import com.gadgetmart.ui.order.model.DataModelArray
import com.gadgetmart.util.PreferenceManager
import com.onesignal.OneSignal
import io.branch.referral.Branch
import io.fabric.sdk.android.Fabric
import java.util.*


class Global:Application() {
    var dataModelValue:DataModelArray?=null
    companion object {
        var context: Context? = null
    }

    var isResume=true

    var isLoading=true

    var varitionID=""
    var productID=""
    private val mNotificationTime = Calendar.getInstance().timeInMillis + 10000 //Set after 5 seconds from the current time.
    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        // Branch logging for debugging
        Branch.enableDebugMode()

        // Branch object initialization

        // Branch object initialization
        Branch.getAutoInstance(this)
        // OneSignal Initialization
        OneSignal.startInit(this)
            .setNotificationOpenedHandler(AppNotificationOpenedHandler())
            .setNotificationReceivedHandler(AppNotificationReceivedHandler())
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None)
            .init()

        OneSignal.idsAvailable { userId, registrationId ->
            Log.d("debug", "User:$userId")
            if (registrationId != null)
                PreferenceManager(this)?.setOneSignalNotificationId(userId)
            Log.e("debug", "registrationId:$registrationId")
        }
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun notificationSchdule(base: Context,name:String){
        Log.e("notification",PreferenceManager().getInstance(this).getIsNotification())
        if(PreferenceManager().getInstance(this).getIsNotification()!!.equals("false")){
            NotificationUtils().setNotification(mNotificationTime, base,name!!)
            PreferenceManager().getInstance(this).setIsNotification("true")
        }
    }
    fun cancelNotification(base: Context){
        if(PreferenceManager().getInstance(this).getIsNotification().equals("true")){
            NotificationUtils().cancelNotification(base)
            PreferenceManager().getInstance(this).setIsNotification("false")
        }
    }
}