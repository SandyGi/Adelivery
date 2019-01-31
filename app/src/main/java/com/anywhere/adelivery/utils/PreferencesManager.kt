package com.anywhere.adelivery.utils

import android.content.Context
import android.content.SharedPreferences


class PreferencesManager(var context: Context) {

    val PREFS_FILENAME = "com.anywhere.adelivery"
    val PREF_USER_ID = "userId"
    val PREF_USER_NAME = "userName"
    val PREF_OTP = "otp"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var userId: String
        get() = prefs.getString(PREF_USER_ID, "DefaultUser")
        set(value) = prefs.edit().putString(PREF_USER_ID, value).apply()

    var getOtp: String
        get() = prefs.getString(PREF_OTP, "1234")
        set(value) = prefs.edit().putString(PREF_OTP, value).apply()

    var getUserName: String
        get() = prefs.getString(PREF_USER_NAME, "1234")
        set(value) = prefs.edit().putString(PREF_USER_NAME, value).apply()
}