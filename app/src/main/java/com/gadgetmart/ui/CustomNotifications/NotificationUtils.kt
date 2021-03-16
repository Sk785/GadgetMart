package com.gadgetmart.ui.CustomNotifications

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.Toast
import java.util.*

/**
 * Created by devdeeds.com on 5/12/17.
 */

class NotificationUtils {
    var alarmManager:AlarmManager?=null

    fun setNotification(timeInMilliSeconds: Long, activity: Context,name:String) {

        //------------  alarm settings start  -----------------//

        if (timeInMilliSeconds > 0) {

             alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(activity.applicationContext, AlarmReceiver::class.java) // AlarmReceiver1 = broadcast receiver

            alarmIntent.putExtra("name", name)
            alarmIntent.putExtra("timestamp", timeInMilliSeconds)



            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMilliSeconds
//            Toast.makeText(activity,"call", Toast.LENGTH_SHORT).show()
            val repeatInterval = AlarmManager.INTERVAL_DAY * 2
            val triggerTime = (SystemClock.elapsedRealtime()
                    + repeatInterval)

            // val futureInMillis: Long = SystemClock.elapsedRealtime() +  60*1000
            val pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager!!.setInexactRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() +
                    triggerTime,  repeatInterval, pendingIntent);
            //alarmManager!!.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)

        }

        //------------ end of alarm settings  -----------------//


    }
    fun cancelNotification(context: Context){
        val intent = Intent()
         alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT or Intent.FILL_IN_DATA)
        alarmManager!!.cancel(pendingIntent)
    }
}