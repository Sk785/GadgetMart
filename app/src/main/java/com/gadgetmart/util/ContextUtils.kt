package com.gadgetmart.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.gadgetmart.util.custom.StyledAlertDialog


class ContextUtils {

    companion object {
        fun hasNetwork(context: Context?): Boolean {

            if (context == null) {
                return false
            }

            var networkInfo: NetworkInfo? = null
            val connectivityManager = context
                .applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                networkInfo = connectivityManager.activeNetworkInfo
            }

            // should check null because in airplane mode it will be null
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }

        fun getAuthToken(context: Context?): String {
            if (context == null){
                return "Sorry, auth token not found as context is null"
            }
            if(PreferenceManager().getInstance(context).getAuthToken()!!.equals("")){
                return ""

            }else{
                return "Bearer " + PreferenceManager().getInstance(context).getAuthToken()!!

            }
            return "Bearer " + PreferenceManager().getInstance(context).getAuthToken()!!
        }

        /**
         * Show alert dialog with title, single-selection list and cancel buttons;
         * set text on the field mentioned
         */
        fun showSingleChoiceAlertDialog(
            context: Context,
            listItems: Array<String>,
            dialogTitle: String,
            view: TextView?
        ) {
            val mBuilder = AlertDialog.Builder(context, StyledAlertDialog.getStyleRes())
            mBuilder.setTitle(dialogTitle)
            mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->
                view!!.text = listItems[i]
                dialogInterface.dismiss()
            }
            // Set the neutral/cancel button click listener
            mBuilder.setNeutralButton("Cancel") { dialog, which ->
                // Do something when click the neutral button
                dialog.cancel()
            }

            val mDialog = mBuilder.create()
            mDialog.show()
        }


    }
}