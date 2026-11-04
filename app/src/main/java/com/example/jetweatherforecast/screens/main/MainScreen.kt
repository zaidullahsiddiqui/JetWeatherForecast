package com.example.jetweatherforecast.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetweatherforecast.data.DataOrException
import com.example.jetweatherforecast.model.Weather
import com.example.jetweatherforecast.model.WeatherObject
import com.example.jetweatherforecast.navigation.WeatherScreens
import com.example.jetweatherforecast.screens.setting.SettingsViewModel
import com.example.jetweatherforecast.util.format
import com.example.jetweatherforecast.util.formatDecimals
import com.example.jetweatherforecast.widgets.HumidityWindPressureRow
import com.example.jetweatherforecast.widgets.SunsetSunriseRow
import com.example.jetweatherforecast.widgets.WeatherAppBar
import com.example.jetweatherforecast.widgets.WeatherDetailRow
import com.example.jetweatherforecast.widgets.WeatherStateImage

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    city: String?
) {
    val unitFromDb = settingsViewModel.unitList.collectAsState().value
    val unit = if (unitFromDb.isNotEmpty()) {
        unitFromDb[0].unit.split(" ")[0].lowercase()
    } else {
        "imperial"
    }
    val isImperial = unit == "imperial"

    val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
        initialValue = DataOrException(loading = true),
        key1 = city,
        key2 = unit
    ) {
        value = mainViewModel.getWeatherData(city = city.toString(), units = unit)
    }.value

    if (weatherData.loading == true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF1E88E5))
        }
    } else if (weatherData.data != null) {
        MainScaffold(
            weather = weatherData.data!!,
            navController = navController,
            isImperial = isImperial
        )
    } else if (weatherData.e != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Error loading weather: ${weatherData.e?.localizedMessage ?: "Unknown error"}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun MainScaffold(
    weather: Weather,
    navController: NavController,
    isImperial: Boolean
) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F2FE),
            Color(0xFFF1F5F9)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                WeatherAppBar(
                    title = weather.city.name + ", " + weather.city.country,
                    navController = navController,
                    onAddActionClicked = {
                        navController.navigate(WeatherScreens.SearchScreen.name)
                    },
                    elevation = 0.dp
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                MainContent(data = weather, isImperial = isImperial)
            }
        }
    }
}

@Composable
fun MainContent(data: Weather, isImperial: Boolean) {
    val iconCode = data.list[0].weather[0].icon
    val imageUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"

    Column(
        Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = format(data.list[0].dt),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF475569),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Surface(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .size(190.dp),
            shape = CircleShape,
            color = Color(0xFFF59E0B),
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFFBBF24), Color(0xFFF59E0B))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WeatherStateImage(imageUrl = imageUrl, modifier = Modifier.size(75.dp))
                    Text(
                        text = formatDecimals(data.list[0].temp.day) + "°",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 42.sp
                        ),
                        color = Color.White
                    )
                    Text(
                        text = data.list[0].weather[0].main,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = FontStyle.Italic
                        ),
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        HumidityWindPressureRow(weather = data.list[0], isImperial = isImperial)
        SunsetSunriseRow(weather = data.list[0])

        Text(
            text = "This Week",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF1E293B),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight(),
            color = Color.White.copy(alpha = 0.6f),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(6.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(items = data.list) { item: WeatherObject ->
                    WeatherDetailRow(weather = item)
                }
            }
        }
    }
}
