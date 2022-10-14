package com.gadgetmart.ui.CustomNotifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import com.gadgetmart.R
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    var context:Context?=null
    private lateinit var mNotification: Notification
    private val mNotificationId: Long = 120910L

    override fun onReceive(context: Context, intent: Intent) {
        this.context=context
//        val service = Intent(context, NotificationService::class.java)
//        service.putExtra("name", intent.getStringExtra("name"))
//        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))
//        context.startService(service)

        onHandleIntent(intent)
    }


    @SuppressLint("NewApi")
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(NotificationService.CHANNEL_ID, NotificationService.CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#e8334a")
            notificationChannel.description = ""
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun onHandleIntent(intent: Intent?) {
        //Create Channel
        createChannel()
        var timestamp: Long = 0
        var name= ""
        if (intent != null && intent.extras != null) {
            timestamp = intent.extras!!.getLong("timestamp")
            name = intent.extras!!.getString("name").toString()
        }

        if (timestamp > 0) {
            var notificationManager: NotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notifyIntent = Intent()

            val title = "Don’t be the one who missed out"
            val message = "Hey "+name+" ,still interested in product you put in your cart. Buy the product before you lose it!"

            notifyIntent.putExtra("title", title)
            notifyIntent.putExtra("message", message)
            notifyIntent.putExtra("notification", true)

            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp


            val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val res = context!!.resources
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                mNotification = Notification.Builder(context, NotificationService.CHANNEL_ID)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(Notification.BigTextStyle()
                        .bigText(message))
                    .setContentText(message).build()
            } else {
                mNotification = Notification.Builder(context)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle(title)
                    .setStyle(Notification.BigTextStyle()
                        .bigText(message))
                    .setSound(uri)
                    .setContentText(message).build()

            }
            notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // mNotificationId is a unique int for each notification that you must define
            notificationManager.notify(mNotificationId.toInt(), mNotification)
        }
    }



}