package com.example.weatherapp.presentation

import android.content.Intent // Import necesario para usar Intent
import com.example.weatherapp.R
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {

    private val apiKey = "0719aee3bf7f7f54b2c5cec301c7290e" // Reemplaza con tu API Key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextCity = findViewById<EditText>(R.id.editTextCity)
        val buttonGetWeather = findViewById<Button>(R.id.buttonGetWeather)
        val textViewWeather = findViewById<TextView>(R.id.textViewWeather)
        val buttonNext = findViewById<Button>(R.id.buttonNext) // Referencia al botón "Next"

        buttonGetWeather.setOnClickListener {
            val city = editTextCity.text.toString()

            if (city.isNotEmpty()) {
                getWeatherData(city, textViewWeather)
            } else {
                textViewWeather.text = "Escribe una ciudad"
            }
        }

        // Configura la acción al hacer clic en el botón "Next"
        buttonNext.setOnClickListener {
            // Aquí puedes definir lo que deseas que suceda al hacer clic en el botón "Next"
            // Por ejemplo, navegar a otra actividad llamada NextActivity
            val intent = Intent(this@MainActivity, SecondActivityWear::class.java)
            startActivity(intent)
        }
    }

    private fun getWeatherData(city: String, textViewWeather: TextView) {
        val call = RetrofitClient.apiService.getWeatherData(city, apiKey)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weather = response.body()
                    val tempMin = weather?.main?.temp_min
                    val tempMax = weather?.main?.temp_max
                    val humidity = weather?.main?.humidity

                    textViewWeather.text = """
                        Min Temp: $tempMin°C
                        Max Temp: $tempMax°C
                        Humedad: $humidity%
                    """.trimIndent()
                } else {
                    textViewWeather.text = "Ciudad no encontrada"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                textViewWeather.text = "Hubo un error al recibir los datos"
                Log.e("MainActivity", t.message.toString())
            }
        })
    }
}
