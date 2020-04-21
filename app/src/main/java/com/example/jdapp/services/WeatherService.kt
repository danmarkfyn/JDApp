package com.example.jdapp.services

import android.content.ContentValues
import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.net.URL
import kotlin.reflect.typeOf


// TODO https://www.androdocs.com/tutorials/creating-an-android-weather-app-using-kotlin.html
class WeatherService(city: String) : AsyncTask<String, Void, String>() {
    val API: String = "d2bf47110bd268a41ae0088c1c3e7d56"
    var city = city

    override fun doInBackground(vararg params: String): String {
        var result: String
        try {
            result =
                URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
        } catch (e: Exception) {

            Log.d(ContentValues.TAG, "Error while trying to get weather: " + e)
            result = "No Weather Data"
        }
        val jsonObj = JSONObject(result)

        val main = jsonObj.getJSONObject("main")

        val temp = main.getString("temp") + "°C"

        Log.d(ContentValues.TAG, "Temp: " + temp)
        return temp
    }
}




