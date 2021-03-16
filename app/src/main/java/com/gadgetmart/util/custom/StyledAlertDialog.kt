package com.gadgetmart.util.custom

import android.content.Context
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import com.gadgetmart.R


class StyledAlertDialog {
    companion object {
        fun builder(context: Context): AlertDialog.Builder {
            return AlertDialog.Builder(context, getStyleRes())
        }

        @StyleRes
        fun getStyleRes(): Int {
            return R.style.AlertDialogStyle
        }
    }
}