package com.example.jetweatherforecast.model

data class City(
    val cord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val timezone: Int
)