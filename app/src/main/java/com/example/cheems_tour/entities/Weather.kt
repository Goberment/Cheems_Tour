package com.example.cheems_tour.entities

import com.google.gson.annotations.SerializedName

class Weather {
    @SerializedName("name")
    var name: String? = null
}

//Modelo de datos minimalista para mapear la respuesta de OpenWeatherMap.
// Solo mapea el campo 'name' (nombre de la ciudad tal como lo devuelve la API).
//Paquete	com.example.cheems_tour.entities
//Anotación	@SerializedName("name") — indica a Gson que la propiedad 'name' de la clase corresponde a la clave JSON 'name'.
//name: String?	Nombre de la ciudad retornado por OpenWeatherMap.
