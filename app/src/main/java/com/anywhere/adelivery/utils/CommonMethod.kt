package com.anywhere.adelivery.utils

import android.app.Activity
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
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

        fun getDistance(latA: Double, logA: Double, latB: Double, logB: Double): Int {
            val locationA = Location("point A")

            locationA.latitude = latA
            locationA.longitude = logA

            val locationB = Location("point B")

            locationB.latitude = latB
            locationB.longitude = logB

            val distance = locationA.distanceTo(locationB) / 1000//To convert Meter in Kilometer
            return Math.round(distance)
        }

        fun getLocationFromAddress(context: Context, strAddress: String): LatLng? {

            val coder = Geocoder(context)
            val address: List<Address>?
            var p1: LatLng? = null

            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress, 5)
                if (address == null) {
                    return null
                }

                val location = address[0]
                p1 = LatLng(location.latitude, location.longitude)

            } catch (ex: IOException) {

                ex.printStackTrace()
            }

            return p1
        }

        fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val theta = lon1 - lon2
            var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + (Math.cos(deg2rad(lat1))
                    * Math.cos(deg2rad(lat2))
                    * Math.cos(deg2rad(theta)))
            dist = Math.acos(dist)
            dist = rad2deg(dist)
            dist = dist * 60.0 * 1.1515
            return dist
        }

        private fun deg2rad(deg: Double): Double {
            return deg * Math.PI / 180.0
        }

        private fun rad2deg(rad: Double): Double {
            return rad * 180.0 / Math.PI
        }

    }
}