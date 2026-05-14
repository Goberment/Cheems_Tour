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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.internal.IGoogleMapDelegate
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.log

private fun GoogleMap?.moveCamera(update: Any) {
    TODO("Not yet implemented")
}

class TripFormActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {

    var map: GoogleMap? = null
    lateinit var btnsave : Button
    lateinit var name : EditText
    lateinit var city : EditText
    var latitude : Double = 0.0
    var longitude : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trip_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        name  = findViewById(R.id.txt_name)
        city = findViewById(R.id.txt_city)
        btnsave = findViewById(R.id.btn_save)
        btnsave.setOnClickListener(this)

        var  mapFragment = supportFragmentManager.findFragmentById(R.id.map_form) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            map = googleMap
            map!!.mapType = GoogleMap.MAP_TYPE_HYBRID
            map?.clear()

            var latLng = LatLng(0.0,0.0)

            map?.addMarker(MarkerOptions().position(latLng).draggable(true))
            map?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            map?.animateCamera(CameraUpdateFactory.zoomTo(8f))

            map?.setOnMarkerDragListener(object: GoogleMap.OnMarkerDragListener {
                override fun onMarkerDrag(p0: Marker) {
                    TODO("Not yet implemented")
                }

                override fun onMarkerDragEnd(marker: Marker) {
                    val latLng = marker.position
                    latitude = latLng.latitude
                    longitude = latLng.longitude

                    Log.d("Latitude", latitude.toString())
                    Log.d("Longitude", longitude.toString())

                }

                override fun onMarkerDragStart(p0: Marker) {
                    TODO("Not yet implemented")
                }
            })

        } catch (ex: Exception){
            Log.e("Error loading map", "Error")
        }
    }
}