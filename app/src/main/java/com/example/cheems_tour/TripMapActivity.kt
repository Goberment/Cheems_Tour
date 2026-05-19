package com.example.cheems_tour

import android.Manifest
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
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission

class TripMapActivity : AppCompatActivity(), OnMapReadyCallback {

    var map: GoogleMap? = null
    val markerTripMap = mutableMapOf<Marker, Trip>()

    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trip_map)
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun getTrips() {
        val call: Call<List<Trip>> = RetrofitUtil.getApi().getTrips()
        call.enqueue(object : Callback<List<Trip>> {
            override fun onResponse(call: Call<List<Trip>>, response: Response<List<Trip>>) {
                val trips: List<Trip> = response.body() ?: emptyList()
                trips.forEach { trip ->
                    val latLng = LatLng(trip.latitude, trip.longitude)
                    val marker = map?.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(trip.name)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.cheems))
                    )
                    marker?.let { markerTripMap[it] = trip }
                }
            }
            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
                Log.e("Error calling API", t.message.toString())
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

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun vibrate(durationMs: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    durationMs,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(durationMs)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            map = googleMap
            map!!.mapType = GoogleMap.MAP_TYPE_NORMAL

            map!!.setOnMarkerClickListener { marker ->
                vibrate(40)
                val trip = markerTripMap[marker]
                if (trip != null) {
                    getWeather(trip.latitude, trip.longitude, trip.name ?: "")
                    marker.showInfoWindow()
                }
                true
            }

            map!!.setOnInfoWindowClickListener {  marker ->
                vibrate(40)
                val trip = markerTripMap[marker]
                if (trip != null) {
                    val intent = Intent(this, TripFormActivity::class.java)
                    intent.putExtra("trip", trip)
                    startActivity(intent)
                }
            }

            getTrips()

        } catch (ex: Exception) {
            Log.e("Error loading map", ex.message.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        if (map != null) {
            map?.clear()
            markerTripMap.clear()
            getTrips()
        }
    }
}