package com.example.cheems_tour.interfaces

import com.example.cheems_tour.entities.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenweatherAPI {

    @GET("Weather")
    fun getWeather(@Query("lat")lat: Double,
                   @Query("lon") lon: Double,
                   @Query("appid") appId: String): Call<Weather>
}