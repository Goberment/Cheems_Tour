package com.example.cheems_tour.interfaces

import com.example.cheems_tour.entities.Trip
import retrofit2.http.GET
import retrofit2.Call

interface CheemsAPI {


    @GET("trips")
    fun getTrips() : Call<List<Trip>>



}