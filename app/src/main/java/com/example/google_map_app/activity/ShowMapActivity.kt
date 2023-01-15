package com.example.google_map_app.activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.google_map_app.MainActivity
import com.example.google_map_app.R
import com.example.google_map_app.map.MapCompanion
import com.example.google_map_app.map.MapCompanion.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.example.google_map_app.map.MapsActivity
import com.example.google_map_app.util.PermissionUtils
import kotlinx.android.synthetic.main.activity_show_map.*

class ShowMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_map)

        view_map_button.setOnClickListener {
           // startActivity(Intent(this@ShowMapActivity, MainActivity::class.java))
            when {
                PermissionUtils.isAccessFineLocationGranted(this) -> {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                           // startActivity(Intent(this@ShowMapActivity, MapsActivity::class.java))
                            startActivity(Intent(this@ShowMapActivity, MapsActivity::class.java))
                            // setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                }
                else -> {
                    PermissionUtils.requestAccessFineLocationPermission(
                        this,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
      /*  when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                       // setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    MapCompanion.LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }*/
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            startActivity(Intent(this@ShowMapActivity, MapsActivity::class.java))
                           // setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}