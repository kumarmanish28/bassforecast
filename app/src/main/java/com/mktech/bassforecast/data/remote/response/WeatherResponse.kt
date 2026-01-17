package com.mktech.bassforecast.data.remote.response

data class WeatherResponse(
    val current: CurrentWeather,
    val hourly: HourlyWeather
)

data class CurrentWeather(
    val temperature_2m: Double,
    val weathercode: Int,
    val wind_speed_10m: Double
)

data class HourlyWeather(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val wind_speed_10m: List<Double>,
    val weathercode: List<Int>
)
