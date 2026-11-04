package com.example.jetweatherforecast.model

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("coord")
    val cord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val timezone: Int
)