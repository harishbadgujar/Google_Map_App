package com.example.google_map_app.util

import android.location.Location
import android.text.format.DateFormat
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.*


object LocationInfo {

    fun getGeofenceEnterExitTime() : String {
        val currentTime: String = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
        return currentTime
    }
    fun String.getDate(time: Long): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time * 1000
        return DateFormat.format("dd-MM-yyyy hh:mm:ss a", cal).toString()
    }
    fun LatLng.calculateDistance(endLocation: LatLng): Double {
        val startPoint = Location("locationA")
        startPoint.latitude = this.latitude
        startPoint.longitude = this.longitude
        val endPoint = Location("locationB")
        endPoint.latitude = endLocation.latitude
        endPoint.longitude = endLocation.longitude
        return startPoint.distanceTo(endPoint).toDouble()
    }
    /*fun getGeofenceEnterExitTime(myLambda: (String) -> Unit) {
        val currentTime: String = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
        return myLambda(currentTime)
    }*/
}