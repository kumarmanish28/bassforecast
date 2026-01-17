package com.mktech.bassforecast.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mktech.bassforecast.state.WeatherUiState
import com.mktech.bassforecast.ui.component.CurrentlyWeather
import com.mktech.bassforecast.ui.component.HourlyForecastRow
import com.mktech.bassforecast.ui.theme.Primary
import com.mktech.bassforecast.ui.theme.Top_Label
import com.mktech.bassforecast.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: WeatherViewModel, onRetry: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val city by viewModel.cityName.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(modifier = Modifier.wrapContentSize()) {
                        Text(
                            text = "CURRENT LOCATION",
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Top_Label
                        )
                        Text(
                            modifier = Modifier.offset(y = (-6).dp),
                            text = city,
                            fontSize = 22.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White
                        )
                    } },
                colors = TopAppBarColors(
                    titleContentColor = Color.White,
                    containerColor = Primary,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified,
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(color = Primary)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp, vertical = 5.dp)
        ) {

            when (val state = uiState) {
                is WeatherUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment =  Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is WeatherUiState.Success -> {
                    CurrentlyWeather(state.currentWeather)
                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "Hourly Forecast",
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(state.hourly.size) { index ->
                            val hourlyForecast = state.hourly[index]
                            HourlyForecastRow(hourlyForecast)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                is WeatherUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment =  Alignment.Center) {
                        Column(modifier = Modifier.wrapContentSize()) {
                            Text(text = state.message)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onRetry, modifier = Modifier.width(140.dp)) {
                                Text(
                                    text = "Retry",
                                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
