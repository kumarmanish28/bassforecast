🌤️ BassForecast - Weather Forecast App
A clean, modern Android weather application built with Jetpack Compose that displays current weather and 24-hour forecasts using the Open-Meteo API.

📱 Features
Current Weather Display: Shows temperature, weather condition, and wind speed

24-Hour Forecast: Hour-by-hour weather predictions with 12-hour AM/PM format

Responsive UI: Built with Jetpack Compose with proper state management

Error Handling: Graceful error states with retry functionality

MVVM Architecture: Follows Android recommended architecture pattern

Location Services: Automatic location detection with fallback to default

🛠️ Tech Stack
Language: Kotlin

UI: Jetpack Compose

Architecture: MVVM (Model-View-ViewModel)

Networking: Retrofit with Kotlin Coroutines

State Management: StateFlow

Location: Fused Location Provider

DI: Manual Dependency Injection


📋 Requirements
API: Open-Meteo (https://api.open-meteo.com)

Minimum SDK: Android 8.0 (API 26)

Compile SDK: Android 16 (API 36)

🚀 Key Implementation Details
1. API Integration
Uses Open-Meteo API (no API key required)

Handles parallel arrays from API response

Maps to domain objects using WeatherMapper

Limited to 24 hours forecast as per requirements



