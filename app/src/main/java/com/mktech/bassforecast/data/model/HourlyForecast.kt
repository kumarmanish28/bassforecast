package com.mktech.bassforecast.data.model

data class HourlyForecast(
    val time: String,
    val temperature: String,
    val windSpeed: String,
    val code: Int
)