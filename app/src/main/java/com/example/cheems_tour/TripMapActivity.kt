package com.example.cheems_tour

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cheems_tour.entities.Trip
import com.example.cheems_tour.utils.RetrofitUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.cheems_tour.entities.Weather

class TripMapActivity : AppCompatActivity(), OnMapReadyCallback {

    var map : GoogleMap? = null
    val markerTripMap = mutableMapOf<Marker, Trip>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trip_map)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getTrips()
    }
    fun getTrips(){
        val call : Call<List<Trip>> = RetrofitUtil.getApi().getTrips()
        call.enqueue(object : Callback<List<Trip>> {
            override fun onResponse(
                call: Call<List<Trip>>,
                response: Response<List<Trip>>
            ) {
                val trips: List<Trip> = response.body() ?: emptyList()
                trips.forEach { trip ->
                    val latLng = LatLng(trip.latitude, trip.longitude)
                    val marker = map?.addMarker(MarkerOptions().position(latLng).title(trip.name).icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.cheems)))
                    marker?.let { markerTripMap[it] = trip }
                }
            }
            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
                Log.e("Error calling API ", t.message.toString())
            }
        })
    }
    fun getWeather(lat: Double, lon: Double, markerTitle: String) {
        val call: Call<Weather> = RetrofitUtil.getApiWeather().getWeather(
            lat, lon,
            "6e701c7593390c023ca1567c653a4e2e" // tu API key de OpenWeather
        )
        call.enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                val weather = response.body()
                if (weather != null) {
                    val temp = weather.temp?.let { "%.1f°C".format(it - 273.15) } ?: "N/A"
                    val desc = weather.description ?: "Sin descripción"
                    val humidity = weather.humidity?.let { "$it%" } ?: "N/A"

                    Toast.makeText(
                        this@TripMapActivity,
                        "📍 $markerTitle\n🌡 $temp  💧 $humidity\n☁ $desc",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Log.e("Error weather API", t.message.toString())
                Toast.makeText(
                    this@TripMapActivity,
                    "Error obteniendo clima",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            map = googleMap
            map!!.mapType = GoogleMap.MAP_TYPE_NORMAL

            // Click en el icono cheems → pide el clima
            map!!.setOnMarkerClickListener { marker ->
                val trip = markerTripMap[marker]
                if (trip != null) {
                    getWeather(trip.latitude, trip.longitude, trip.name ?: "")
                }
                true
            }

            getTrips()

        } catch (ex: Exception) {
            Log.e("Error loading map", ex.message.toString())
        }
    }

}