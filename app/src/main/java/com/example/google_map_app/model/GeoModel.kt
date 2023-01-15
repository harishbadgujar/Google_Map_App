package com.example.google_map_app.model

import com.google.android.gms.location.Geofence

data class GeoModel(
    var geofenceTag : String?=null,
    var enterTimeOfArea:Long=0,
    var exitTimeOfArea:Long=0,
    var spendTimeInArea:String?=null
)