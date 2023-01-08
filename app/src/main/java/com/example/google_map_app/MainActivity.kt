package com.example.google_map_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.google_map_app.map.repository.MapRepository
import com.example.google_map_app.map.utils.MapViewModelFactory
import com.example.google_map_app.map.viewmodel.MapViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MapViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //how to set geofence
        setUpViewModel()
        observViewModel()
    }

    private fun observViewModel() {
        viewModel.let {
            it.latlong.observe(this@MainActivity){
              Toast.makeText(this,it.latitude.toString()+"|||"+it.longitude.toString(),Toast.LENGTH_SHORT).show()
            }
            it.getCurrentLocationInfo()
        }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this, MapViewModelFactory(MapRepository(this@MainActivity),this@MainActivity))[MapViewModel::class.java]
    }
}