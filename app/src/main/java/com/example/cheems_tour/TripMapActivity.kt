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



//Activity que muestra un Google Map de tipo NORMAL con todos los viajes cargados desde la API.
// Cada viaje se representa con un marker personalizado (imagen de Cheems).
// Al tocar la ventana de información de un marker, abre TripFormActivity pasando el viaje seleccionado para su edición.
class TripMapActivity : AppCompatActivity(), OnMapReadyCallback {

    var map : GoogleMap? = null
    //Referencia al objeto GoogleMap una vez que el mapa esté listo. Nullable porque se inicializa de forma asíncrona.

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

    //•	Infla el layout activity_trip_map (que solo contiene un SupportMapFragment a pantalla completa).
    //•	Aplica insets de ventana.
    //•	Obtiene la referencia al SupportMapFragment y llama a getMapAsync(this) para inicializar el mapa de forma asíncrona.
    //•	Llama a getTrips() para iniciar la descarga de viajes (puede ejecutarse antes de que el mapa esté listo;
    // los markers se añaden en onMapReady o cuando ambos datos estén disponibles).


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

    //Método: getTrips()
    //Realiza una llamada GET /trips a la CheemsAPI. Al recibir la lista de viajes:
    //•	Itera sobre cada Trip.
    //•	Crea un LatLng con la latitud y longitud del viaje.
    //•	Añade un marker en el mapa con la imagen R.drawable.cheems como ícono personalizado
    // (usando BitmapDescriptorFactory.fromResource).
    //•	Guarda el objeto Trip en marker.tag para recuperarlo al hacer clic.


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

    //Callback que se invoca cuando el mapa está completamente inicializado:
    //•	Asigna el googleMap recibido a la propiedad map.
    //•	Configura el tipo de mapa como MAP_TYPE_NORMAL.
    //•	Registra un OnInfoWindowClickListener: cuando el usuario pulsa en la ventana de info de un marker,
// extrae el Trip guardado en marker.tag, crea un Intent hacia TripFormActivity con el Trip
}