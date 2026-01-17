🌤️ Bass Forecast – Weather App (Android)

Bass Forecast is a modern Android weather application built using Jetpack Compose and MVVM architecture.
It fetches real-time weather data based on the user’s current location and displays hourly forecasts in a clean, dark-themed UI.

🚀 Features

📍 Current location-based weather

🌡️ Real-time temperature, wind speed, and conditions

🕒 Hourly weather forecast (next 24 hours)

🗺️ City name detection using Geocoder

📡 Open-Meteo API integration

🔐 Runtime location permission handling

⚡ Manual Dependency Injection (No Hilt/Dagger)

🎨 Jetpack Compose UI

🏗️ Architecture

The app follows MVVM (Model–View–ViewModel):

UI (Compose) → ViewModel → API / Location Manager

Key Principles:

UI does NOT contain business logic

ViewModel handles all data & state

Dependencies are injected manually from Activity

No Clean Architecture layers (simple MVVM)

📁 Package Structure
com.mktech.bassforecast

├── data
│   ├── model
│   └── remote
│
├── state
│
├── ui
│
├── utils
│
└── viewmodel

🔌 Tech Stack

Kotlin

Jetpack Compose

MVVM

Retrofit

Coroutines + StateFlow

Google Location Services

Open-Meteo API

📍 Permissions Used
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>

🌐 Weather API

Data is fetched from:

https://api.open-meteo.com/v1/forecast


Example parameters:

latitude

longitude

current: temperature, weather code, wind speed

hourly: forecast data

🧠 Manual Dependency Injection

No Hilt or Dagger is used.
All dependencies (API, LocationManager) are provided from MainActivity using:

WeatherViewModelFactory

Constructor injection

This keeps the app:

Lightweight

Easy to understand

Test-friendly

🖥️ UI Preview

The UI includes:

Current location & temperature header

Hourly forecast cards

Weather icons

Wind information

Dark modern design

▶️ How to Run

Clone the repository

Open in Android Studio

Sync Gradle

Run on a real device (location required)

Allow location permission

⚠️ Notes

Location works best on real devices (not emulator)

Internet connection is required

If permission is denied, a default location is used

👨‍💻 Author

Manish Kumar
Android Developer | Kotlin | Jetpack Compose
