package com.gadgetmart.app

import android.util.Log
import com.onesignal.OSNotification
import com.onesignal.OneSignal

class AppNotificationReceivedHandler: OneSignal.NotificationReceivedHandler {
    override fun notificationReceived(notification: OSNotification?) {
        Log.d("Payload data: ", notification?.payload?.rawPayload.toString())

    }
}