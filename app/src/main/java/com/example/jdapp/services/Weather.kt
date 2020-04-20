package com.example.jdapp.services

import android.content.ContentValues
import android.os.AsyncTask
import android.util.Log
import java.net.URL


// TODO https://www.androdocs.com/tutorials/creating-an-android-weather-app-using-kotlin.html
class Weather(city: String) : AsyncTask<String, Void, String>() {
    val API: String = "d2bf47110bd268a41ae0088c1c3e7d56"
    var city = city

    override fun doInBackground(vararg params: String): String {
        var weatherResponse: String
        try {
            weatherResponse =
                URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
        } catch (e: Exception) {
            weatherResponse = "Weather error" + e
        }
        Log.d(ContentValues.TAG, "Temp: " + weatherResponse)
        return weatherResponse
    }
}




