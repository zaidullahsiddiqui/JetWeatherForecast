package com.example.jetweatherforecast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetweatherforecast.navigation.WeatherNavigation
import com.example.jetweatherforecast.ui.theme.JetWeatherForecastTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherApp()

        }
    }
}

@Composable
fun WeatherApp() {
    JetWeatherForecastTheme {
        Scaffold(modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent) { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){
                WeatherNavigation()
            }

        }
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetWeatherForecastTheme {
    }
}