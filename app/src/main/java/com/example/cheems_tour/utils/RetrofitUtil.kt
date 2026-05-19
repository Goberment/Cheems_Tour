package com.example.cheems_tour.utils

import com.example.cheems_tour.interfaces.CheemsAPI
import com.example.cheems_tour.interfaces.OpenweatherAPI
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtil {
    fun  getApi(): CheemsAPI{
        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(("http://10.0.2.2:5000/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        return retrofit.create(CheemsAPI::class.java)
    }

    fun  getApiWeather(): OpenweatherAPI {
        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        return retrofit.create(OpenweatherAPI::class.java)
    }
}