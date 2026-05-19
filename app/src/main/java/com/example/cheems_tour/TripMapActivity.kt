package com.example.cheems_tour

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripMapActivity : AppCompatActivity(), OnMapReadyCallback {

    var map : GoogleMap? = null

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
                val trips: List<Trip> = response.body()!!
                trips.forEach { t ->
                    val latLng = LatLng(t.latitude, t.longitude)
                    val marker = map?.addMarker(MarkerOptions().position(latLng).title(t.name).icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.cheems)))
                    marker?.tag = t
                }
            }
            override fun onFailure(call: Call<List<Trip>>, t: Throwable) {
                Log.e("Error calling API ", t.message.toString())
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
          try {
              map = googleMap
              map!!.mapType = GoogleMap.MAP_TYPE_NORMAL
              map?.setOnInfoWindowClickListener { marker ->
                  val trip = marker.tag as Trip
                  val intent = Intent(this, TripFormActivity::class.java)
                  intent.putExtra("trip", trip)
                  startActivity(intent)
              }

          }catch (ex: Exception){
              Log.e("Error loading map", ex.message.toString())
          }
    }

}