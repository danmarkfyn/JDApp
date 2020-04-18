package com.example.jdapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions

class HospitalDetailsActivity : AppCompatActivity(),  OnMapReadyCallback {

    private var gMap: GoogleMap? = null
    private var hospitalLat = 55.384035
    private var hospitalLong = 10.368267

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospitaldetails)

        val extras = intent.extras ?: return
        val hospitalSelected = extras.getString("HospitalName")

/*
        val textView = findViewById<TextView>(R.id.textView2)
        textView.text = hospitalSelected


 */
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //setUpMap(gMap!!)

        /*
        (map as SupportMapFragment).getMapAsync{
            gMap = it
            setUpMap(gMap!!)
        }*/

    }

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

    //Used to specifying map type
    private fun setUpMap(map: GoogleMap) {
        map.uiSettings.setZoomControlsEnabled(true)
        map.mapType = GoogleMap.MAP_TYPE_HYBRID

    }

}
