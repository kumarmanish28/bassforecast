package com.mktech.bassforecast

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.mktech.bassforecast.ui.screen.HomeScreen
import com.mktech.bassforecast.ui.theme.BassForecastTheme
import com.mktech.bassforecast.viewmodel.WeatherViewModel
import com.mktech.bassforecast.viewmodel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(application)
    }
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupPermissionLauncher()
        viewModel.initializeLocationManager(permissionLauncher)

//        enableEdgeToEdge()
        setContent {
            BassForecastTheme {
                HomeScreen(
                    viewModel = viewModel,
                    onRetry = {
                        viewModel.retryLocation()
                    }
                )
            }
        }

        viewModel.requestLocationOnce()
    }

    private fun setupPermissionLauncher() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                viewModel.locationManager.getCurrentLocationModern()
            } else {
                showPermissionDeniedMessage()
                viewModel.useDefaultLocation()
            }
        }
    }
    private fun showPermissionDeniedMessage() {
        Toast.makeText(
            this,
            "Location permission is required for accurate weather information",
            Toast.LENGTH_LONG
        ).show()
    }


}