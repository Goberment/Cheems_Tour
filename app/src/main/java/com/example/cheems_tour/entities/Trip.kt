package com.example.cheems_tour.entities

import java.io.Serializable

class Trip : Serializable {
    constructor(id: Int? = null, name: String = "", city: String = "", latitude: Double = 0.0, longitude: Double = 0.0) {
        this.id = id
        this.name = name
        this.city = city
        this.latitude = latitude
        this.longitude = longitude
    }

    var id: Int? = null
    var name: String? = null
    var city: String? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0
}

//Constructor primario con todos los parámetros opcionales con valores por defecto.
//Permite crear Trip() vacío o Trip(id, name, city, latitude, longitude) completo

//id: Int?	Identificador único del viaje asignado por el backend. null en creación.
//name: String?	Nombre descriptivo del viaje (ej. 'Navojork').
//city: String?	Ciudad donde se localiza el viaje (ej. 'Guaymas, Sonora').
//latitude: Double	Latitud geográfica del punto en el mapa. Por defecto 0.0.
//longitude: Double	Longitud geográfica del punto en el mapa. Por defecto 0.0.

