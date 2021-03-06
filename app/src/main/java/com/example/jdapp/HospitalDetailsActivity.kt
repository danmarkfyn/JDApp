package com.example.jdapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.jdapp.services.WeatherService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions

/**
 * This class is used to display more details about hospital selected in DisplayHospitalsActivity
 * @param hospitalLat: Used by GoogleMap to display hospitals latitude
 * @param hospitalLong: Used by GoogleMap to display hospital longitude
 */
class HospitalDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var gMap: GoogleMap? = null
    private var hospitalLat = 0.0
    private var hospitalLong = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospitaldetails)

        //Getting data from intent extras
        val extras = intent.extras ?: return
        val selectedHospitalName = extras.getString("HospitalName")
        val selectedHospitalDescription = extras.getString("HospitalDescription")
        val selectedHospitalCity = extras.getString("HospitalCity")
        val selectedHospitalLat = extras.getString("HospitalLat")
        val selectedHospitalLong = extras.getString("HospitalLong")

        // using WeatherService to get temperature based on the city
        val temp = WeatherService(selectedHospitalCity.toString()).execute().get()

        //Passing data from intent to layout elements
        val textViewName = findViewById<TextView>(R.id.hospitalDetails_hospitalName)
        textViewName.text = getString(R.string.hospitalDetailsActivity_name) + selectedHospitalName
        val textViewDescription = findViewById<TextView>(R.id.hospitalDetails_hospitalDescription)
        textViewDescription.text = getString(R.string.hospitalDetailsActivity_description) + selectedHospitalDescription
        val textViewCity = findViewById<TextView>(R.id.hospitalDetails_hospitalCity)
        textViewCity.text = getString(R.string.hospitalDetailsActivity_city) + selectedHospitalCity
        val textViewTemp = findViewById<TextView>(R.id.hospitalDetails_hospitalTemp)
        textViewTemp.text = getString(R.string.hospitalDetailsActivity_temperature) + temp
        hospitalLat = selectedHospitalLat?.toDouble()!!
        hospitalLong = selectedHospitalLong?.toDouble()!!

        //Map fragement for hospital location
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            gMap = it
            setUpMap(gMap!!)
        }

    }

    //Called when the map is ready
    override fun onMapReady(map: GoogleMap?) {
        gMap = map
        val hospitalPosition = com.google.android.gms.maps.model.LatLng(hospitalLat, hospitalLong)
        val hospitalMarker: MarkerOptions = MarkerOptions().position(hospitalPosition)
        val zoomLevel = 10.0f

        gMap.let {
            it!!.addMarker(hospitalMarker)
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(hospitalPosition, zoomLevel))
        }
    }

    //Specifying map type and zoom enabling
    private fun setUpMap(map: GoogleMap) {
        map.uiSettings.setZoomControlsEnabled(true)
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        onMapReady(gMap)
    }
}
