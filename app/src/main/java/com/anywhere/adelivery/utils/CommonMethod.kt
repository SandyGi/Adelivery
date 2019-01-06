package com.anywhere.adelivery.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.anywhere.adelivery.ui.activity.RegistrationActivity

class CommonMethod {
    companion object {
        fun showCustomToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun showExitDialog(context: Activity) {

            var alertDialog = AlertDialog.Builder(context)
            alertDialog.setMessage("Do you want to exit from application?")

            alertDialog.setCancelable(false)
                .setPositiveButton("Yes") { dialog, which ->
                    context.finish()
                }
                .setNegativeButton("No") { dialog, which ->
                   dialog.dismiss()
                }
            alertDialog.show()
        }
    }
}