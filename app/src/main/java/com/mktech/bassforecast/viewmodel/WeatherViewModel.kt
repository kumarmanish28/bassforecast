package com.mktech.bassforecast.viewmodel

import android.app.Application
import android.location.Geocoder
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mktech.bassforecast.data.model.HourlyForecast
import com.mktech.bassforecast.data.remote.RetrofitClient
import com.mktech.bassforecast.data.remote.response.HourlyWeather
import com.mktech.bassforecast.state.WeatherUiState
import com.mktech.bassforecast.utils.LocationManager
import com.mktech.bassforecast.utils.MyConstant.DEFAULT_LATITUDE
import com.mktech.bassforecast.utils.MyConstant.DEFAULT_LONGITUDE
import com.mktech.bassforecast.utils.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.math.roundToInt

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val api = RetrofitClient.weatherApiService

    // UI State
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

    private val _cityName = MutableStateFlow<String>("Base Forecast")
    val cityName: StateFlow<String> = _cityName

    // Location
    private lateinit var locationManager: LocationManager
    private var locationPermissionLauncher: ActivityResultLauncher<String>? = null

    // State tracking
    private var hasInitializedLocation = false
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    fun initializeLocationManager(permissionLauncher: ActivityResultLauncher<String>) {
        this.locationPermissionLauncher = permissionLauncher

        locationManager = LocationManager(
            context = context,
            onLocationReceived = { lat, lon ->
                // This callback runs on background thread
                viewModelScope.launch(Dispatchers.Main) {
                    setLocationAndFetch(lat, lon)
                }
            },
            onError = { errorMessage ->
                viewModelScope.launch(Dispatchers.Main) {
                    _uiState.value = WeatherUiState.Error(errorMessage)
                    // Fallback to default after delay
                    kotlinx.coroutines.delay(2000)
                    useDefaultLocation()
                }
            },
            onPermissionDenied = {
                viewModelScope.launch(Dispatchers.Main) {
                    _uiState.value = WeatherUiState.Error("Location permission denied")
                }
            },
            onLocationServicesDisabled = {
                // Activity can handle this dialog
                // We'll just use default location
                viewModelScope.launch(Dispatchers.Main) {
                    useDefaultLocation()
                }
            }
        )

        locationManager.initialize(permissionLauncher)
    }


    fun requestLocationOnce() {
        if (!hasInitializedLocation) {
            locationManager.requestLocationWithPermissionCheck()
            hasInitializedLocation = true
        } else {
            // Already initialized, just use current data
            Log.d("WeatherViewModel", "Location already initialized, not requesting again")
        }
    }

    fun retryLocation() {
        locationManager.requestLocationWithPermissionCheck()
    }

    fun setLocationAndFetch(latitude: Double, longitude: Double) {
        val currentState = _uiState.value
        if (currentState is WeatherUiState.Success &&
            currentLatitude == latitude &&
            currentLongitude == longitude) {
            Log.d("WeatherViewModel", "Already have data for this location, skipping")
            return
        }

        currentLatitude = latitude
        currentLongitude = longitude
        fetchAllData(latitude, longitude)
    }

    private fun fetchAllData(latitude: Double, longitude: Double) {
        fetchWeather(latitude, longitude)
        fetchCityName(latitude, longitude)
    }

    private fun fetchCityName(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val city = getCityNameFromCoordinates(latitude, longitude)
                _cityName.value = city
            } catch (e: Exception) {
                _cityName.value = "Current Location"
            }
        }
    }

    private suspend fun getCityNameFromCoordinates(
        latitude: Double,
        longitude: Double
    ): String = withContext(Dispatchers.IO) {
        return@withContext try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            addresses?.firstOrNull()?.let { address ->
                address.locality ?: address.subAdminArea ?: address.adminArea ?: "Current Location"
            } ?: "Current Location"
        } catch (e: Exception) {
            "(${String.format("%.2f", latitude)}, ${String.format("%.2f", longitude)})"
        }
    }

    fun fetchWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val response = api.getWeatherForecast(
                    latitude = latitude,
                    longitude = longitude,
                    current = "temperature_2m,weathercode,wind_speed_10m",
                    hourly = "temperature_2m,weathercode,wind_speed_10m"
                )

                val hourlyList = mapHourlyData(response.hourly)

                _uiState.value = WeatherUiState.Success(
                    currentWeather = response.current,
                    hourly = hourlyList,
                )

            } catch (e: Exception) {
                val errorMessage = when {
                    e is java.net.UnknownHostException -> "No internet connection"
                    e is java.net.SocketTimeoutException -> "Connection timeout"
                    e is retrofit2.HttpException -> when (e.code()) {
                        401 -> "Authentication error"
                        404 -> "Weather service not available"
                        500 -> "Server error"
                        else -> "Network error: ${e.code()}"
                    }
                    else -> "Failed to load weather data: ${e.message}"
                }

                _uiState.value = WeatherUiState.Error(errorMessage)
            }
        }
    }

    fun retryWithCurrentLocation() {
        currentLatitude?.let { lat ->
            currentLongitude?.let { lon ->
                fetchWeather(lat, lon)
            }
        } ?: run {
            // If no current location, request new location
            retryLocation()
        }
    }

    fun useDefaultLocation() {
        setLocationAndFetch(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
    }

    private fun mapHourlyData(hourly: HourlyWeather): List<HourlyForecast> {
        return (0 until minOf(24, hourly.time.size)).map { i ->
            HourlyForecast(
                time = Utility.convertTo12HourFormat(hourly.time[i]),
                temperature = "${hourly.temperature_2m[i].roundToInt()}°C",
                windSpeed = "${hourly.wind_speed_10m[i].roundToInt()} km/h",
                code = hourly.weathercode[i]
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationManager.cleanup()
    }
}
