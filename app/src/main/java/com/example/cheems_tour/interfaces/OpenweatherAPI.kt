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

//Interfaz Retrofit para la API pública de OpenWeatherMap.
// Define un único endpoint para consultar el clima por coordenadas.
//GET	/Weather	Consulta el clima por coordenadas. Parámetros @Query:
// lat (Double), lon (Double), appid (String — API key).