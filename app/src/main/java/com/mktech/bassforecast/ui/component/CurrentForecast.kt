package com.mktech.bassforecast.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mktech.bassforecast.data.remote.response.CurrentWeather
import com.mktech.bassforecast.ui.theme.CardBg
import com.mktech.bassforecast.utils.MyConstant
import kotlin.math.roundToInt

@Composable
fun CurrentlyWeather(currentWeather: CurrentWeather) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBg
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.TopEnd),
                painter = painterResource(MyConstant.getWeatherIcon(currentWeather.weathercode)),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {

                Text(
                    text = "Currently Forecast",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${currentWeather.temperature_2m.roundToInt()}",
                        fontSize = 36.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )

                    Text(
                        text = "o",
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        modifier = Modifier
                            .offset(y = (-13).dp)
                            .padding(horizontal = 2.dp)
                    )

                    Text(
                        text = "C",
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Column(
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(
                        text = "WIND",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${currentWeather.wind_speed_10m.roundToInt()}",
                            fontSize = 22.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White
                        )
                        Text(
                            text = "km/h",
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White,
                            modifier = Modifier
                                .offset(y = (2).dp)
                                .padding(horizontal = 2.dp)
                        )

                    }
                }

            }
        }
    }
}