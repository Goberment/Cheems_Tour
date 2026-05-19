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
    var tripId: Int? = null
    lateinit var btnsave: Button
    lateinit var btndelete: Button
    lateinit var name: EditText
    lateinit var city: EditText
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    //map: GoogleMap?	Referencia al mapa embebido. Nullable por inicialización asíncrona.
   // tripId: Int?	ID del viaje en edición. null si se está creando uno nuevo.
    //btnsave: Button	Botón de guardar. Lateinit porque se inicializa en onCreate.
    //btndelete: Button	Botón de eliminar. Lateinit.
    //name: EditText	Campo de texto para el nombre del viaje. Lateinit.
    //city: EditText	Campo de texto para la ciudad del viaje. Lateinit.
    //latitude: Double	Coordenada latitud actualizada cuando el usuario suelta el marker. Valor inicial 0.0.
    //longitude: Double	Coordenada longitud actualizada cuando el usuario suelta el marker. Valor inicial 0.0.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_trip_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //•	Infla el layout activity_trip_form.
        //•	Obtiene referencias a los campos de texto y botones.
        //•	Registra this como OnClickListener de ambos botones.
        //•	Lee el extra 'trip' del Intent (puede ser null si se viene desde el botón 'Add trip').
        // Si existe, rellena los campos con los datos del viaje y guarda su ID en tripId.
        //•	Inicializa el SupportMapFragment embebido (R.id.map_form) y registra el callback asíncrono con getMapAsync(this).


        name = findViewById(R.id.txt_name)
        city = findViewById(R.id.txt_city)
        btnsave = findViewById(R.id.btn_save)
        btnsave.setOnClickListener(this)
        btndelete = findViewById(R.id.btn_delete)
        btndelete.setOnClickListener(this)

        val trip = intent.getSerializableExtra("trip") as Trip?
        if (trip != null) {
            tripId = trip.id
            name.setText(trip.name)
            city.setText(trip.city)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_form) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    //•	Establece el tipo de mapa como MAP_TYPE_HYBRID.
   // •	Limpia cualquier marker previo con map.clear().
    //•	Crea un LatLng en (0.0, 0.0) y añade un marker arrastrable (draggable=true) en esa posición.
    //•	Mueve la cámara al (0,0) con zoom 8.
   // •	Registra un OnMarkerDragListener: en onMarkerDragEnd, actualiza las propiedades latitude y
    // longitude con la nueva posición del marker y las registra en logcat.

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

    override fun onClick(v: View?) {
        val tripName = name.text.toString()
        val tripCity = city.text.toString()

        //Maneja los clics de los dos botones. Primero lee el texto de name y city. Luego, según el botón pulsado:

        when(v?.id) {
            R.id.btn_delete -> {
                val call = RetrofitUtil.getApi().deleteTrip(tripId!!)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) finish()
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("Error", t.message.toString())
                    }
                })
            }

            //•	Llama a RetrofitUtil.getApi().deleteTrip(tripId!!) — solo ejecutable si tripId no es null (modo edición).
            //•	En onResponse exitoso, llama a finish() para cerrar la Activity y volver al mapa.

            R.id.btn_save -> {
                if (tripName.isEmpty() || tripCity.isEmpty()) return
                val trip = Trip(name = tripName, city = tripCity, latitude = latitude, longitude = longitude)
                if (tripId != null) {
                    val call = RetrofitUtil.getApi().updateTrip(tripId!!, trip)
                    call.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) finish()
                        }
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("Error", t.message.toString())
                        }
                    })

                    //•	Valida que name y city no estén vacíos; si lo están, retorna sin hacer nada.
                    //•	Construye un objeto Trip con los datos del formulario y las coordenadas del marker.
                    //•	Si tripId != null: llama a updateTrip(tripId!!, trip) — PUT al backend.
                    //•	Si tripId == null: llama a createTrip(trip) — POST al backend.
                    //•	En ambos casos, en onResponse exitoso llama a finish().
                    // 
                } else {
                    val call = RetrofitUtil.getApi().createTrip(trip)
                    call.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) finish()
                        }
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("Error", t.message.toString())
                        }
                    })
                }
            }
        }
    }
}