package com.example.cheems_tour

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cheems_tour.entities.Trip
import com.example.cheems_tour.entities.Weather
import com.example.cheems_tour.utils.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.RadioButton

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnTripMap = findViewById<View>(R.id.btnTripMap) as Button
        btnTripMap.setOnClickListener(this)
        val btnTripForm = findViewById<View>(R.id.btnTripForm) as Button
        btnTripForm.setOnClickListener(this)


        //•	enableEdgeToEdge() — activa el modo edge-to-edge para
        // que el contenido se extienda bajo las barras del sistema.
       // •	setContentView(R.layout.activity_main) — infla el layout XML principal.
        //•	ViewCompat.setOnApplyWindowInsetsListener(...) —
        // aplica padding para que el contenido no quede tapado por la barra de estado o la barra de navegación.
        //•	Encuentra los botones btnTripMap y btnTripForm y registra this como su OnClickListener.
        //•	Lee la preferencia de idioma actual desde SharedPreferences ('settings', clave 'lang').
        //•	Marca el RadioButton correspondiente al idioma activo (rbEnglish o rbSpanish).
        //•	Registra listeners en los RadioButtons: al hacer clic,
        // guardan el idioma en SharedPreferences y llaman a recreate()
        // para reiniciar la Activity y aplicar el nuevo idioma.


        // ---- NUEVO ----
        val rbEnglish = findViewById<RadioButton>(R.id.rbEnglish)
        val rbSpanish = findViewById<RadioButton>(R.id.rbSpanish)

        val currentLang = getSharedPreferences("settings", MODE_PRIVATE)
            .getString("lang", "en") ?: "en"
        if (currentLang == "es") rbSpanish.isChecked = true
        else rbEnglish.isChecked = true

        rbEnglish.setOnClickListener {
            getSharedPreferences("settings", MODE_PRIVATE).edit()
                .putString("lang", "en").apply()
            recreate()
        }

        rbSpanish.setOnClickListener {
            getSharedPreferences("settings", MODE_PRIVATE).edit()
                .putString("lang", "es").apply()
            recreate()
        }
        // ---- FIN NUEVO ----

    }

    fun getWeather(){
        val call : Call<Weather> = RetrofitUtil.getApiWeather().getWeather(27.9481064,
            -110.9329378,
            "6e701c7593390c023ca1567c653a4e2e")

        call.enqueue(object : Callback<Weather>{
            override fun onResponse(call: Call<Weather?>?, response: Response<Weather?>) {
                val weather: Weather = response.body()!!
                var a : Int = 1
            }

            override fun onFailure(call: Call<Weather?>, t: Throwable) {
                Log.e("Error calling API", t.message.toString())
            }
        })
    }

    //Realiza una llamada asíncrona a la API de OpenWeatherMap para obtener el clima de las coordenadas
    // de Guaymas, Sonora (lat: 27.9481064, lon: -110.9329378).
    // Usa la API key '6e701c7593390c023ca1567c653a4e2e'.
    // Actualmente este método no está conectado a ningún elemento de la UI;
    //  los datos se reciben pero solo se declara una variable local sin uso adicional.
    //  Está pensado para mostrar el clima en tiempo real (el TextView 'Weather' en el layout hace referencia a esta
    //  funcionalidad).



    override fun onClick(view: View) {
        when(view.id){
            R.id.btnTripMap -> {
                val intentMap = Intent(this, TripMapActivity::class.java)
                startActivity(intentMap)
            }
            R.id.btnTripForm -> {
                val intentMap = Intent(this, TripFormActivity::class.java)
                startActivity(intentMap)
            }
        }
    }
}
//Maneja los clics de los dos botones principales usando un bloque when sobre view.id

//btnTripMap	Crea un Intent hacia TripMapActivity y lo lanza con startActivity().
//btnTripForm	Crea un Intent hacia TripFormActivity y lo lanza con startActivity().
