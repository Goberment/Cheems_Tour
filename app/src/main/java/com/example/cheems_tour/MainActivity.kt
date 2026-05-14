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