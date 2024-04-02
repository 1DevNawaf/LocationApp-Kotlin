package com.example.locationapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class LocationUtils(private val context:Context) {
    fun hasLocationPermission(context: Context) : Boolean{
        return (
                    //checks fine location
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )== PackageManager.PERMISSION_GRANTED
                    &&
                    //checks coarse location
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )== PackageManager.PERMISSION_GRANTED
                )
    }
}