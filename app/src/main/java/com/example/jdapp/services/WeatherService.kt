package com.example.jdapp.services

import android.content.ContentValues
import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.net.URL
import kotlin.reflect.typeOf

/**
 * This class is used to return a temperature based on a provided city name.
 * It is launched asynchronously to the main thread
 * @param city the name of the city to look up the temperature for. A String
 */
class WeatherService(city: String) : AsyncTask<String, Void, String>() {
    val API: String = "d2bf47110bd268a41ae0088c1c3e7d56" // API key from https://api.openweathermap.org/
    var city = city

    override fun doInBackground(vararg params: String): String {
        var result: String
        Log.d(ContentValues.TAG, "Running URL Call")

        // access the API through given URL with city value and API key
        try {
            result =
                URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "Error while trying to get weather: " + e)
            result =
                "No Weather Data"
        }
        try {

            // read the result as a JSON object
            val jsonObj = JSONObject(result)
            val main = jsonObj.getJSONObject("main")
            val temp = main.getString("temp") + "°C"

            Log.d(ContentValues.TAG, "Temp: " + temp)
            return temp
        } catch (e: Exception){
            Log.d(ContentValues.TAG, "Error: " + e)
        }
        return "n/a"
    }
}




