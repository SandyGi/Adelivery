package com.anywhere.adelivery.utils

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.Toast

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
    }
}