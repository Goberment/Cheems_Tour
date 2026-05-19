package com.example.cheems_tour.interfaces

import com.example.cheems_tour.entities.Trip
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CheemsAPI {


    @GET("trips")
    fun getTrips() : Call<List<Trip>>


    @GET("trip/{id}")
        fun getTrip(@Path("id") id: Int): Call<Trip>

    @POST("trip")
    fun createTrip(@Body trip: Trip): Call<Void>

    @PUT("trip/{id}")
    fun updateTrip(@Path("id") id: Int, @Body trip: Trip): Call<Void>

    @DELETE("trip/{id}")
    fun deleteTrip(@Path("id") id: Int): Call<Void>



}