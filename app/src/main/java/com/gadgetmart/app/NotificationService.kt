package com.gadgetmart.app

import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import com.gadgetmart.R
import com.onesignal.NotificationExtenderService
import com.onesignal.OSNotificationReceivedResult
import java.math.BigInteger


class NotificationService : NotificationExtenderService() {
    override fun onNotificationProcessing(notification: OSNotificationReceivedResult?): Boolean {
        // Read properties from result.

        // Return true to stop the notification from displaying.

//        overrideSettings.extender =
//            NotificationCompat.Extender { NotificationCompat.Builder(this, "message_notification") }

        val overrideSettings = OverrideSettings()
        overrideSettings.extender = NotificationCompat.Extender { builder ->
            // Sets the background notification color to Red on Android 5.0+ devices.
            val icon = BitmapFactory.decodeResource(
                resources,
                R.mipmap.ic_launcher
            )
            builder.setLargeIcon(icon)
            builder.setColor(BigInteger("FFFF0000", 16).toInt())
        }

        val displayedResult = displayNotification(overrideSettings)
        Log.d("notification: ","Notification displayed with id: ${displayedResult.androidNotificationId}")
        return false
    }
}