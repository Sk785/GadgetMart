package com.gadgetmart.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {

        fun formatTime(timeInMillis: Long): String {
            val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            return dateFormat.format(timeInMillis * 1000)
        }

        fun formatTime1(timeInMillis: Long): String {
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            return dateFormat.format(timeInMillis)
        }

        fun formatTimeWithMarker(timeInMillis: Long): String {
            val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            return dateFormat.format(timeInMillis)
        }

        fun getHourOfDay(timeInMillis: Long): Int {
            val dateFormat = SimpleDateFormat("H", Locale.getDefault())
            return Integer.valueOf(dateFormat.format(timeInMillis))
        }

        fun getMinute(timeInMillis: Long): Int {
            val dateFormat = SimpleDateFormat("m", Locale.getDefault())
            return Integer.valueOf(dateFormat.format(timeInMillis))
        }

        /**
         * If the given time is of a different date, display the date.
         * If it is of the same date, display the time.
         * @param timeInMillis  The time to convert, in milliseconds.
         * @return  The time or date.
         */
        fun formatDateTime(timeInMillis: Long): String {
            return if (isToday(timeInMillis)) {
                formatTime(timeInMillis)
            } else {
                formatDate(timeInMillis)
            }
        }

        /**
         * Formats timestamp to 'date month' format (e.g. 'February 3').
         */
        fun formatDate(timeInMillis: Long): String {
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return dateFormat.format(timeInMillis * 1000)
        }

        fun convertStringDate(timeInMillis: String): String {
            val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

            val date: Date = format.parse(timeInMillis)
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return dateFormat.format(date)
        }

        fun formatDate1(timeInMillis: Long): String {
            val dateFormat = SimpleDateFormat("yyyy-MMM-dd HH:mm", Locale.getDefault())
            return dateFormat.format(timeInMillis)
        }

        fun newdate(timeInMillis: Long): String {
            val dateFormat = SimpleDateFormat("dd MMM,yyyy", Locale.getDefault())
            return dateFormat.format(timeInMillis * 1000)
        }

        /**
         * Returns whether the given date is today, based on the user's current locale.
         */
        fun isToday(timeInMillis: Long): Boolean {
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val date = dateFormat.format(timeInMillis)
            return date == dateFormat.format(System.currentTimeMillis())
        }


        /**
         * Checks if two dates are of the same day.
         * @param millisFirst   The time in milliseconds of the first date.
         * @param millisSecond  The time in milliseconds of the second date.
         * @return  Whether {@param millisFirst} and {@param millisSecond} are off the same day.
         */
        fun hasSameDate(millisFirst: Long, millisSecond: Long): Boolean {
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            return dateFormat.format(millisFirst * 1000) == dateFormat.format(millisSecond * 1000)
        }

        fun hasSameDate1(millisFirst: Long, millisSecond: Long): Boolean {
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            return dateFormat.format(millisFirst) == dateFormat.format(millisSecond)
        }


        fun isTimeBetweenTwoTime(
            initialTime: String, finalTime: String,
            currentTime: String
        ): Boolean {
            Log.e("time", initialTime + " " + finalTime + " " + currentTime)
            var calendar1 = Calendar.getInstance()

            if (changeTimeIntoSeconds(initialTime) == true) {
                var time1 = SimpleDateFormat("hh:mm:ss").parse(initialTime)
                calendar1.time = time1;
                calendar1.add(Calendar.DATE, 1);
            } else {
                var time1 = SimpleDateFormat("mm:ss").parse(initialTime)
                calendar1.time = time1;
                calendar1.add(Calendar.DATE, 1);
            }

            var calendar2 = Calendar.getInstance()

            if (changeTimeIntoSeconds(finalTime) == true) {
                var time2 = SimpleDateFormat("hh:mm:ss").parse(finalTime)
                calendar2.time = time2;
                calendar2.add(Calendar.DATE, 1);
            } else {
                var time2 = SimpleDateFormat("mm:ss").parse(finalTime)
                calendar2.time = time2;
                calendar2.add(Calendar.DATE, 1);
            }

            var calendar3 = Calendar.getInstance()

            if (changeTimeIntoSeconds(currentTime) == true) {
                var d = SimpleDateFormat("hh:mm:ss").parse(currentTime)
                calendar3.time = d;
                calendar3.add(Calendar.DATE, 1);
            } else {
                var d = SimpleDateFormat("mm:ss").parse(currentTime)
                calendar3.time = d;
                calendar3.add(Calendar.DATE, 1);
            }


            var x = calendar3.time
            if (x.after(calendar1.time) && x.before(calendar2.time)) {
                //checkes whether the current time is between 14:49:00 and 20:11:13.
                return true
            }
            return false
        }

        fun changeTimeIntoSeconds(time: String?): Boolean {
            Log.e("time", time);
            val units = time!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray() //will break the string up into an array

            if (units.size == 2) {

                return false


            } else if (units.size == 3) {

                return true


            }


            return false


        }

        fun makeArrayList(value: String): ArrayList<String> {
            return ArrayList<String>(Arrays.asList(*value.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
        }

    }


}