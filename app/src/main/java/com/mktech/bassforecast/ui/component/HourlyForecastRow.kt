package com.mktech.bassforecast.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mktech.bassforecast.data.model.HourlyForecast
import com.mktech.bassforecast.ui.theme.CardBg
import com.mktech.bassforecast.ui.theme.DarkBg
import com.mktech.bassforecast.utils.MyConstant
import com.mktech.bassforecast.utils.Utility

@Composable
fun HourlyForecastRow(hourlyForecast: HourlyForecast) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBg
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .size(36.dp)
                    .background(color = DarkBg, shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .size(32.dp),
                    painter = painterResource(MyConstant.getWeatherIcon(hourlyForecast.code)),
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .wrapContentSize()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = hourlyForecast.time,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
                Spacer(Modifier.padding(vertical = 2.dp))
                Text(
                    text = MyConstant.getWeatherLabel(hourlyForecast.code) ,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(120.dp)
                )
            }

            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically)
            {
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = hourlyForecast.temperature,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
            }
            VerticalDivider(
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 5.dp),
                thickness = 1.dp,
                color = Color.Gray
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(),
                horizontalAlignment = Alignment.End
            ) {
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .offset(y = (-10).dp),
                    painter = painterResource(MyConstant.getWeatherIcon(hourlyForecast.code)),
                    contentDescription = null
                )
                Text(
                    text = "WIND",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = hourlyForecast.windSpeed,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                }
            }
        }
    }

}




