package com.example.locationapp

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.locationapp.ui.theme.LocationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel:LocationViewModel = viewModel()
            LocationAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun MyApp(viewModel: LocationViewModel) {
    val context = LocalContext.current
    val locationUtils= LocationUtils(context)
    LocationDisplay(locationUtils = locationUtils,viewModel, context = context)

}


@Composable
fun LocationDisplay(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    context: Context
) {
    val location = viewModel.location.value

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION]==true
                && permissions[Manifest.permission.ACCESS_COARSE_LOCATION]==true){
                //we have the access
                locationUtils.requestLocationUpdates(viewModel)
            }else{
                //Ask for location
                //this is why we need the location
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                        ||ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

                //this will display why we need it
                if (rationaleRequired){
                    Toast.makeText(
                        context,
                        "Location is required for this feature",
                        Toast.LENGTH_LONG
                    ).show()
                }else{
                    //this when the user is denies the access then he need to go to the phone settings
                    Toast.makeText(
                        context,
                        "Location is required for this feature, enable it in the phone settings",
                        Toast.LENGTH_LONG).show()
                }

            }
        }
    )


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (location!=null)
            Text(text = "Address: ${location.latitude} ${location.longitude}")
        else
            Text(text = "Location is not available")

        Button(
            onClick = {
                if (locationUtils.hasLocationPermission(context)){
                    //Permission already granted, update the location
                    locationUtils.requestLocationUpdates(viewModel)
                }else{
                    //Request location permission
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )

                }
            }
        ) {
            Text(text = "Get Location")
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Get Location")
        }
    }
}