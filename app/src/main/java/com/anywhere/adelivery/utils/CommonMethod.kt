package com.anywhere.adelivery.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast

class CommonMethod{
    companion object {
        fun showCustomToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}