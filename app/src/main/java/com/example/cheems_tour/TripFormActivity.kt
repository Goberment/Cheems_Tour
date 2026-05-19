package com.example.cheems_tour

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cheems_tour.entities.Trip
import com.example.cheems_tour.utils.RetrofitUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripFormActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {

    var map: GoogleMap? = null
    lateinit var btnsave: Button
    lateinit var name: EditText
    lateinit var city: EditText
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trip_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        name = findViewById(R.id.txt_name)
        city = findViewById(R.id.txt_city)
        btnsave = findViewById(R.id.btn_save)
        btnsave.setOnClickListener(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_form) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onClick(v: View?) {
        val tripName = name.text.toString()
        val tripCity = city.text.toString()

        Log.d("DEBUG", "name: $tripName, city: $tripCity, lat: $latitude, lon: $longitude")

        if (tripName.isEmpty() || tripCity.isEmpty()) return

        val trip = Trip(name = tripName, city = tripCity, latitude = latitude, longitude = longitude)

        val call = RetrofitUtil.getApi().createTrip(trip)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d("DEBUG", "code: ${response.code()}")
                if (response.isSuccessful) {
                    finish()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            map = googleMap
            map!!.mapType = GoogleMap.MAP_TYPE_HYBRID
            map?.clear()

            val latLng = LatLng(0.0, 0.0)

            map?.addMarker(MarkerOptions().position(latLng).draggable(true))
            map?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            map?.animateCamera(CameraUpdateFactory.zoomTo(8f))

            map?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDrag(p0: Marker) {}

                override fun onMarkerDragEnd(marker: Marker) {
                    val latLng = marker.position
                    latitude = latLng.latitude
                    longitude = latLng.longitude
                    Log.d("Latitude", latitude.toString())
                    Log.d("Longitude", longitude.toString())
                }

                override fun onMarkerDragStart(p0: Marker) {}
            })

        } catch (ex: Exception) {
            Log.e("Error loading map", "Error")
        }
    }
}