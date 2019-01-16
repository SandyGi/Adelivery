package com.anywhere.adelivery.utils

import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE


object PreferencesManager {

    private val APP_SETTINGS = "APP_SETTINGS"


    // properties
    const val PREF_USER_ID = "USER_ID"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    }

    fun getStringValue(context: Context): String?? {
        return getSharedPreferences(context).getString(PREF_USER_ID, null)
    }

    fun setStringValue(context: Context, newValue: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(PREF_USER_ID, newValue)
        editor.apply()
    }

    // other getters/setters
}// other properties...