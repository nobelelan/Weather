package com.parulson.apikotlinweatherappcodingwithevan

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONObject
import java.net.URL
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val CITY: String = "dhaka,bd"
    val API: String = "b3d4514c6eee4ba8d5f59c4bbca8f4dd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()
    }

    inner class weatherTask(): AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.pbLoader).visibility = View.VISIBLE
            findViewById<ConstraintLayout>(R.id.mainConroller).visibility = View.GONE
            findViewById<TextView>(R.id.tvErrorText).visibility = View.GONE
        }
        override fun doInBackground(vararg p0: String?): String? {
            var response: String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)
            }catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: "+SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt * 1000))
                val temp = main.getString("temp")
                val tempMin = "Min Temp: "+main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: "+main.getString("temp_max")+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name")+", "+sys.getString("country")

                findViewById<TextView>(R.id.tvAddress).text = address
                findViewById<TextView>(R.id.tvUpdatedAt).text = updatedAtText
                findViewById<TextView>(R.id.tvStatus).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.tvTemp).text = temp
                findViewById<TextView>(R.id.tvMinTemp).text = tempMin
                findViewById<TextView>(R.id.tvMaxTemp).text = tempMax
                findViewById<TextView>(R.id.tvSunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.tvSunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.tvWind).text = windSpeed
                findViewById<TextView>(R.id.tvPressure).text = pressure
                findViewById<TextView>(R.id.tvHumidity).text = humidity

                findViewById<ProgressBar>(R.id.pbLoader).visibility = View.GONE
                findViewById<ConstraintLayout>(R.id.mainConroller).visibility = View.VISIBLE

            }catch (e:Exception){
                findViewById<ProgressBar>(R.id.pbLoader).visibility = View.GONE
                findViewById<ConstraintLayout>(R.id.mainConroller).visibility = View.GONE
                findViewById<TextView>(R.id.tvErrorText).visibility = View.VISIBLE
            }
        }

    }
}