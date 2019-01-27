package com.anywhere.adelivery.utils

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val ORDER_ID = "ORDER_ID"

class CommonMethod {
    companion object {
        fun showCustomToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun showExitDialog(context: Activity) {

            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setMessage("Do you want to exit from application?")

            alertDialog.setCancelable(false)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    context.finish()
                }
                .setNegativeButton(android.R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
            alertDialog.show()
        }

        fun changeDateFormat(inputString: String, inputFormat: String, outputFormat: String): String? {

            val inputFormatDate = SimpleDateFormat(inputFormat, Locale.US)
            val outputFormatDate = SimpleDateFormat(outputFormat, Locale.US)

            var date: Date? = null
            var str: String? = null

            try {
                date = inputFormatDate.parse(inputString)
                str = outputFormatDate.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return str
        }

    }
}