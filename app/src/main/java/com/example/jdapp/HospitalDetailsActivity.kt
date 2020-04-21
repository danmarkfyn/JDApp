package com.example.jdapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions

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

        //Passing data from intent to layout elements
        val textViewName = findViewById<TextView>(R.id.hospitalDetails_hospitalName)
        textViewName.text = selectedHospitalName
        val textViewDescription = findViewById<TextView>(R.id.hospitalDetails_hospitalDescription)
        textViewDescription.text = selectedHospitalDescription
        val textViewCity = findViewById<TextView>(R.id.hospitalDetails_hospitalCity)
        textViewCity.text = selectedHospitalCity
        hospitalLat = selectedHospitalLat?.toDouble()!!
        hospitalLong = selectedHospitalLong?.toDouble()!!

        //Map fragement for hospital location
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            gMap = it
            setUpMap(gMap!!)
        }

    }

    //Creating marker and showing hospital location
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
