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