package com.example.cheems_tour.entities

import com.google.gson.annotations.SerializedName

class Weather {
    @SerializedName("name")
    var name: String? = null

    // Temperatura en Kelvin (OpenWeather la manda así por defecto)
    @SerializedName("main")
    var main: Main? = null

    @SerializedName("weather")
    var weatherList: List<WeatherDesc>? = null

    // Accesos directos para facilitar el uso
    val temp: Double? get() = main?.temp
    val humidity: Int? get() = main?.humidity
    val description: String? get() = weatherList?.firstOrNull()?.description

    class Main {
        @SerializedName("temp")
        var temp: Double? = null

        @SerializedName("humidity")
        var humidity: Int? = null
    }

    class WeatherDesc {
        @SerializedName("description")
        var description: String? = null
    }
}