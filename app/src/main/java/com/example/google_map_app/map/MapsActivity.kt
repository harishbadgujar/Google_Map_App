package com.example.google_map_app.map

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.google_map_app.R
import com.example.google_map_app.databinding.ActivityMapsBinding
import com.example.google_map_app.map.repository.MapRepository
import com.example.google_map_app.map.utils.MapViewModelFactory
import com.example.google_map_app.map.viewmodel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var myLocationMarker: Marker?=null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapViewModel
    private val geofence = LatLng(18.547800,73.925910)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        observViewModel()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }
    private fun setGeoFenceTergetArea() {
        mMap.let {
            it.addMarker(MarkerOptions().position(geofence).title("Alert" ))
            it.addCircle(CircleOptions()
                .center(geofence)
                .radius(100.0)
                .strokeColor(Color.RED)
                .fillColor(Color.parseColor("#46F44336")))
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setGeoFenceTergetArea()
    }
    private fun observViewModel() {
        viewModel.let {
            it.latlong.observe(this@MapsActivity){
               focusToMyLocation(it)
                if (calculateDistance(it,geofence)<70){
                    Toast.makeText(this,"YOU ARE IN DENGER",Toast.LENGTH_LONG).show()
                }
            }
            it.getCurrentLocationInfo()
        }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this, MapViewModelFactory(MapRepository(this@MapsActivity),this@MapsActivity))[MapViewModel::class.java]
    }

   fun focusToMyLocation(latLng:LatLng){
       val zoomLevel = 16.0f; //This goes up to 21
       myLocationMarker?.let{
           it.position = latLng
       }?: kotlin.run {
           myLocationMarker =   mMap.addMarker(MarkerOptions().position(latLng).title("I am  Here" ))
           mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
       }
   }

    fun calculateDistance(startLocation: LatLng,endLocation: LatLng):Double{
        val startPoint = Location("locationA")
        startPoint.latitude = startLocation.latitude
        startPoint.longitude = startLocation.longitude
        val endPoint = Location("locationA")
        endPoint.latitude = endLocation.latitude
        endPoint.longitude = endLocation.longitude
        return startPoint.distanceTo(endPoint).toDouble()
    }
}