package com.mktech.bassforecast.state

import com.mktech.bassforecast.data.model.HourlyForecast
import com.mktech.bassforecast.data.remote.response.CurrentWeather

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(
        val currentWeather: CurrentWeather,
        val hourly: List<HourlyForecast>
    ) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
