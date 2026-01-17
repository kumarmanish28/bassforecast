package com.mktech.bassforecast.utils

import com.mktech.bassforecast.R

object MyConstant {
    const val BASE_URL = "https://api.open-meteo.com "
    const val DEFAULT_LATITUDE = 28.485355
    const val DEFAULT_LONGITUDE =  77.059257
    fun getWeatherIcon(code: Int): Int {
        return when (code) {

            // 0 - Clear sky
            0 -> R.drawable.clear_sky

            // 1, 2, 3 - Mainly clear, partly cloudy, overcast
            1, 2, 3 -> R.drawable.cloud_fog

            // 45, 48 - Fog
            45, 48 -> R.drawable.cloud_fog

            // 51, 53, 55 - Drizzle
            51, 53, 55 -> R.drawable.drizzle

            // 56, 57 - Freezing Drizzle
            56, 57 -> R.drawable.drizzle

            // 61, 63, 65 - Rain
            61, 63, 65 -> R.drawable.rain

            // 66, 67 - Freezing Rain
            66, 67 -> R.drawable.rain

            // 71, 73, 75 - Snowfall
            71, 73, 75 -> R.drawable.show

            // 77 - Snow grains
            77 -> R.drawable.show

            // 80, 81, 82 - Rain showers
            80, 81, 82 -> R.drawable.thunderstorm

            // 85, 86 - Snow showers
            85, 86 -> R.drawable.show

            // 95 - Thunderstorm
            95 -> R.drawable.thunderstorm

            // 96, 99 - Thunderstorm with hail
            96, 99 -> R.drawable.thunderstorm

            else -> R.drawable.unknown
        }
    }
    fun getWeatherLabel(code: Int): String {
        return when (code) {

            0 -> "Clear Sky"

            1, 2, 3 -> "Partly Cloudy"

            45, 48 -> "Fog"

            51, 53, 55 -> "Drizzle"

            56, 57 -> "Freezing Drizzle"

            61, 63, 65 -> "Rain"

            66, 67 -> "Freezing Rain"

            71, 73, 75 -> "Snowfall"

            77 -> "Snow Grains"

            80, 81, 82 -> "Rain Showers"

            85, 86 -> "Snow Showers"

            95 -> "Thunderstorm"

            96, 99 -> "Thunderstorm with Hail"

            else -> "Unknown"
        }
    }

}