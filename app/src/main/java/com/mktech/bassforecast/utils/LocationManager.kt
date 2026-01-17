package com.mktech.bassforecast.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mktech.bassforecast.utils.MyConstant.DEFAULT_LATITUDE
import com.mktech.bassforecast.utils.MyConstant.DEFAULT_LONGITUDE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
class LocationManager(
    private val context: Context,
    private val onLocationReceived: (Double, Double) -> Unit,
    private val onError: (String) -> Unit = {},
    private val onPermissionDenied: () -> Unit = {},
    private val onLocationServicesDisabled: () -> Unit = {}
) {
    companion object {
        private const val TAG = "LocationManager"
        private const val LOCATION_REQUEST_TIMEOUT_MS = 30000L // Increased timeout for GPS

    }

    private val locationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var locationCallback: LocationCallback
    private var locationUpdateJob: Job? = null
    private var isRequestingLocation = false

    fun initialize(permissionLauncher: ActivityResultLauncher<String>) {
        this.permissionLauncher = permissionLauncher
        setupLocationCallback()
    }

    fun requestLocationWithPermissionCheck() {
        if (hasLocationPermission()) {
            fetchLocation()
        } else {
            Log.d(TAG, "No permission, requesting...")
            requestLocationPermission()
        }
    }

    private fun fetchLocation() {
        if (!isLocationServiceEnabled()) {
            Log.w(TAG, "Location services are disabled")
            onLocationServicesDisabled()
            useDefaultLocation()
            isRequestingLocation = false
            return
        }
        getCurrentLocationWithGPS()
    }


    private fun getCurrentLocationWithGPS() {
        if (!hasLocationPermission()) {
            onError("Location permission not granted")
            isRequestingLocation = false
            return
        }

        val locationRequest = createLocationRequest()

        locationClient.lastLocation
            .addOnSuccessListener { lastLocation ->
                if (lastLocation != null) {
                    handleLocationSuccess(lastLocation)
                } else {
                    startGPSLocationRequest(locationRequest)
                }
            }
            .addOnFailureListener { exception ->
                startGPSLocationRequest(locationRequest)
            }
    }

    private fun startGPSLocationRequest(locationRequest: LocationRequest) {
        try {
            locationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            locationUpdateJob = CoroutineScope(Dispatchers.Main).launch {
                delay(LOCATION_REQUEST_TIMEOUT_MS)
                if (isRequestingLocation) {
                    stopLocationUpdates()
                    onError("GPS location timed out. Make sure you're outdoors or have clear sky view.")
                    useDefaultLocation()
                }
            }
        } catch (securityException: SecurityException) {
            Log.e(TAG, "Security exception: ${securityException.message}")
            onError("Location permission required")
            isRequestingLocation = false
        } catch (e: IllegalStateException) {
            Log.e(TAG, "IllegalStateException: ${e.message}")
            onError("Location service error. Please restart the app.")
            isRequestingLocation = false
        }
    }
    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            LOCATION_REQUEST_TIMEOUT_MS
        ).apply {
            setWaitForAccurateLocation(true)
            setMinUpdateIntervalMillis(1000) // Faster updates for GPS
            setMaxUpdateDelayMillis(2000)
            setMinUpdateDistanceMeters(10f) // Only update if moved 10 meters
        }.build()
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val accuracy = location.accuracy
                    if (accuracy < 150) { // Accept location if accuracy better than 100 meters
                        handleLocationSuccess(location)
                    } else {
                        Log.d(TAG, "Location accuracy poor ($accuracy m), waiting for better fix...")
                    }
                }
            }
        }
    }

    private fun handleLocationSuccess(location: Location) {
        isRequestingLocation = false
        locationUpdateJob?.cancel()
        stopLocationUpdates()
        onLocationReceived(location.latitude, location.longitude)
    }

    fun stopLocationUpdates() {
        try {
            locationClient.removeLocationUpdates(locationCallback)
            locationUpdateJob?.cancel()
            isRequestingLocation = false
        } catch (exception: Exception) {
            Log.d(TAG, "Failed to stop location updates: ${exception.message}")
        }
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun isLocationServiceEnabled(): Boolean {
        return try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            Log.d(TAG, "GPS enabled: $isGpsEnabled, Network enabled: $isNetworkEnabled")
            isGpsEnabled || isNetworkEnabled
        } catch (e: Exception) {
            Log.e(TAG, "Error checking location services: ${e.message}")
            false
        }
    }

    fun openLocationSettings(): Intent {
        return Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    }

    private fun useDefaultLocation() {
        onLocationReceived(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
        isRequestingLocation = false
    }

    fun cleanup() {
        stopLocationUpdates()
        locationUpdateJob?.cancel()
    }

    fun getCurrentLocationModern() {
        if (!hasLocationPermission()) {
            onError("Location permission not granted")
            return
        }

        try {
            locationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null // cancellation token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    onLocationReceived(location.latitude, location.longitude)
                } else {
                    onError("Could not get location")
                }
            }.addOnFailureListener { exception ->
                onError("Location error: ${exception.message}")
            }
        } catch (e: SecurityException) {
            onError("Location permission required")
        }
    }
}