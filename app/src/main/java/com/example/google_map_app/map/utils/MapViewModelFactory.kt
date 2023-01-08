package com.example.google_map_app.map.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.google_map_app.map.repository.MapRepository
import com.example.google_map_app.map.viewmodel.MapViewModel

class MapViewModelFactory(private val mapRepository: MapRepository,val context:Context) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MapViewModel::class.java)){
            return MapViewModel(mapRepository,context) as T
        }
       // return super.create(modelClass)
        throw IllegalArgumentException("Unknown class name")
    }
}