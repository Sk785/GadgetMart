package com.gadgetmart.app

import android.util.Log
import com.gadgetmart.ui.order.OrderDetailsActivity
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal

class AppNotificationOpenedHandler : OneSignal.NotificationOpenedHandler {
    override fun notificationOpened(result: OSNotificationOpenResult?) {

        val actionType = result?.action?.type

        val data = result?.notification?.payload?.additionalData

        if (data != null) {
            try {
                val orderId = data.getString("order_id")
                val notificationType = data.getString("notification_type")
//                val type = data.getString("type")

                Log.d("Payload data: ", data.toString())

                if (notificationType == "order") {
                    OrderDetailsActivity.getPendingStartIntent(
                        Global.context,
                        orderId,
                        notificationType
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}