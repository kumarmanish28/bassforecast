package com.mktech.bassforecast.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mktech.bassforecast.data.remote.WeatherApiService
import com.mktech.bassforecast.utils.LocationManager


class WeatherViewModelFactory(
    private val application: Application,
    private val weatherApi: WeatherApiService,
    private val locationManager: LocationManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(
            application = application,
            weatherApi = weatherApi,
            locationManager = locationManager
        ) as T
    }
}
