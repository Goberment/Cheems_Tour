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

//Interfaz Retrofit que define los endpoints del backend propio (cheemsgo-production.up.railway.app).
// Retrofit genera automáticamente una implementación en tiempo de ejecución.

//GET	/trips	Obtiene la lista completa de viajes. Devuelve Call<List<Trip>>.
//GET	/trip/{id}	Obtiene un viaje específico por ID. Parámetro @Path id: Int. Devuelve Call<Trip>.
//POST	/trip	Crea un nuevo viaje. Cuerpo @Body trip: Trip. Devuelve Call<Void>.
//PUT	/trip/{id}	Actualiza un viaje existente. @Path id + @Body trip. Devuelve Call<Void>.
//DELETE	/trip/{id}	Elimina un viaje por ID. @Path id. Devuelve Call<Void>.
