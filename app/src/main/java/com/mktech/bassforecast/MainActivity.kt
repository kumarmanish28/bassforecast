package com.mktech.bassforecast

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.mktech.bassforecast.data.remote.RetrofitClient
import com.mktech.bassforecast.ui.screen.HomeScreen
import com.mktech.bassforecast.ui.theme.BassForecastTheme
import com.mktech.bassforecast.utils.LocationManager
import com.mktech.bassforecast.viewmodel.WeatherViewModel
import com.mktech.bassforecast.viewmodel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(
            application = application,
            weatherApi = RetrofitClient.weatherApiService,
            locationManager = createLocationManager()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupPermissionLauncher()

        setContent {
            BassForecastTheme {
                HomeScreen(
                    viewModel = viewModel,
                    onRetry = { viewModel.retryLocation() }
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
                viewModel.requestCurrentLocation()
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

    private fun createLocationManager(): LocationManager {
        val manager = LocationManager(
            context = this,
            onLocationReceived = { lat, lon ->
                viewModel.setLocationAndFetch(lat, lon)
            },
            onError = { error ->
                viewModel.useDefaultLocation()
            },
            onPermissionDenied = {
                showPermissionDeniedMessage()
            },
            onLocationServicesDisabled = {
                viewModel.useDefaultLocation()
            }
        )

        manager.initialize(permissionLauncher)

        return manager
    }


}

