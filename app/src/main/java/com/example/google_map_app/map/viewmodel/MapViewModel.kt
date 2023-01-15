package com.example.google_map_app.map.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.google_map_app.map.repository.MapRepository
import com.example.google_map_app.model.GeoModel
import com.example.google_map_app.util.SharedPreferenceHelper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.util.*


class MapViewModel(val mapRepository: MapRepository, private val context: Context) : ViewModel() {

    val latlong=MutableLiveData<LatLng>()
    val sharedPreferenceHelper = SharedPreferenceHelper(context)

    private fun fetchLocation() {
        viewModelScope.launch {
            getCurrentLocationInfo()
        }
    }

    fun saveLocationData(localTime: String){
        sharedPreferenceHelper.putValue(SharedPreferenceHelper.LOCATION_DATA,localTime)
    }

    fun getLocationData(): GeoModel? {
        var geoModel:GeoModel?=null
        sharedPreferenceHelper.getString(SharedPreferenceHelper.LOCATION_DATA)?.let {
            geoModel = Gson().fromJson(it,GeoModel::class.java)
            // geoModel: ArrayList<GeoModel> = Gson().fromJson(it, Array<GeoModel>::class.java)
        }
        geoModel?.let {
           // Log.e("systemMsg112","${getLocationData()?.spendTimeInArea}")
        }

        return geoModel
    }

    fun getCurrentLocationInfo() {

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val locationRequest = com.google.android.gms.location.LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        location?.let {
                            latlong.postValue(LatLng(it.latitude, it.longitude))
                        }
                    }
                    // Few more things we can do here:
                    // For example: Update the location of user on server
                }
            },
            Looper.myLooper()
        )
    }

}