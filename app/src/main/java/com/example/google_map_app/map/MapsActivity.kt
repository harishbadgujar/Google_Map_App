package com.example.google_map_app.map

import android.Manifest
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.google_map_app.R
import com.example.google_map_app.databinding.ActivityMapsBinding
import com.example.google_map_app.map.repository.MapRepository
import com.example.google_map_app.map.utils.MapViewModelFactory
import com.example.google_map_app.map.viewmodel.MapViewModel
import com.example.google_map_app.model.GeoModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*



class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    //123
    private var geofenceArea: Circle? = null
    private var myLocationMarker: Marker? = null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapViewModel
    var geofenceListGeo = ArrayList<Geofence>()
    private val geofence = LatLng(18.547800, 73.925910)
    private val geofence1 = LatLng(18.568860, 73.919550)
    private val geofence2 = LatLng(18.548450, 73.904610)
    private val geofence3 = LatLng(18.549770, 73.929390)
    private var geofenceList = ArrayList<LatLng>()
    var flag = -1
    var geoStatus = ArrayList<GeoModel>()

    // 12
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        observViewModel()
        //createGeoList()
        //addBuildGeoFence()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun addBuildGeoFence() {
        geofenceList.forEach {
            geofenceListGeo.add(
                Geofence.Builder()
                    .setRequestId(UUID.randomUUID().toString())
                    .setCircularRegion(
                        it.latitude,
                        it.longitude,
                        100f
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build()
            )
        }
        val builder = GeofencingRequest.Builder()
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        builder.addGeofences(geofenceListGeo)
        builder.build()
        getGeofencingRequest()
    }

    private fun createGeoList() {
        geofenceList.add(geofence)
        geofenceList.add(geofence1)
        geofenceList.add(geofence2)
        geofenceList.add(geofence3)
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceListGeo)

        }.build()
    }

    private fun setGeoFenceTergetArea() {
        mMap.let {
            it.addMarker(MarkerOptions().position(geofence).title("Alert"))
            geofenceArea = it.addCircle(
                CircleOptions()
                    .center(geofence)
                    .radius(100.0)
                    .strokeColor(Color.RED)
                    .fillColor(Color.parseColor("#46F44336"))
            )
            geofenceArea!!.tag = UUID.randomUUID().toString()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setGeoFenceTergetArea()
        //addBuildGeoFence()
    }

    private fun observViewModel() {
        viewModel.let {
            it.latlong.observe(this@MapsActivity) {
                focusToMyLocation(it)
                if (calculateDistance(it, geofence) < 100) {
                    if (flag == -1) {
                        Toast.makeText(this, "YOU ENTER IN GEOFENCE", Toast.LENGTH_LONG).show()
                        flag = 0
                        geoStatus.apply {
                            this.add(0, GeoModel(geofenceArea?.tag.toString(), System.currentTimeMillis()))
                        }
                    }
                } else {
                    if (flag == 0) {
                        Toast.makeText(this, "YOU EXIT IN GEOFENCE", Toast.LENGTH_LONG).show()
                        flag = -1
                        geoStatus.apply {
                            /*val model =    GeoModel(this.getOrNull(0)?.geofenceTag, this.getOrNull(0)?.enterTimeOfArea ?: 0,
                               System.currentTimeMillis(),
                               getTimeDifferce(this.getOrNull(0)))
                               this.add(0,model)*/

                            val model = GeoModel(this.getOrNull(0)?.geofenceTag,
                                this.getOrNull(0)?.exitTimeOfArea?:0,
                                System.currentTimeMillis(),getTimeDifferce(this.getOrNull(0)))
                            this.add(model)

                        }

                        Log.e("View All Details",geoStatus.getOrNull(0)?.geofenceTag.toString()+"   "+geoStatus.getOrNull(0)?.spendTimeInArea)
                    }
                }
            }
            it.getCurrentLocationInfo()
        }
    }

    private fun getTimeDifferce(orNull: GeoModel?): String {
        val time1 = orNull?.exitTimeOfArea?.toLong() ?: 0
        val time2 = orNull?.enterTimeOfArea?.toLong() ?: 0
        val diffrence = time2 - time1
        val myValue = convertSecondsToHMmSs(diffrence)
        return myValue ?: ""
    }

    fun convertSecondsToHMmSs(millis: Long): String? {
        val seconds = millis / 1000 % 60
        val minutes = millis / (1000 * 60) % 60
        val hours = millis / (1000 * 60 * 60)
        val b = StringBuilder()
        b.append(if (hours == 0L) "00" else if (hours < 10) "0$hours" else hours.toString())
        b.append(":")
        b.append(if (minutes == 0L) "00" else if (minutes < 10) "0$minutes" else minutes.toString())
        b.append(":")
        b.append(if (seconds == 0L) "00" else if (seconds < 10) "0$seconds" else seconds.toString())
        return b.toString()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this, MapViewModelFactory(MapRepository(this@MapsActivity), this@MapsActivity))[MapViewModel::class.java]
    }

    fun focusToMyLocation(latLng: LatLng) {
        val zoomLevel = 16.0f; //This goes up to 21
        myLocationMarker?.let {
            it.position = latLng
        } ?: kotlin.run {
            myLocationMarker = mMap.addMarker(MarkerOptions().position(latLng).title("I am  Here"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
        }
    }

    fun calculateDistance(startLocation: LatLng, endLocation: LatLng): Double {
        val startPoint = Location("locationA")
        startPoint.latitude = startLocation.latitude
        startPoint.longitude = startLocation.longitude
        val endPoint = Location("locationB")
        endPoint.latitude = endLocation.latitude
        endPoint.longitude = endLocation.longitude
        return startPoint.distanceTo(endPoint).toDouble()
    }

}