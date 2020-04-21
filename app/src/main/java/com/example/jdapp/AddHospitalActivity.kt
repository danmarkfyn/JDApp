package com.example.jdapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_hospital.*
import java.io.IOException

class AddHospitalActivity : AppCompatActivity(), OnMapReadyCallback {

    private val myDB = FirebaseFirestore.getInstance()
    private var gMap: GoogleMap? = null

    private lateinit var nameEditText: TextView
    private lateinit var descriptionEditText: TextView
    private lateinit var cityEditText: TextView
    private lateinit var inputSearch: TextView

    private var hospitalName = ""
    private var hospitalDescription = ""
    private var hospitalCity = ""
    private var lat = 55.6
    private var long = 10.1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hospital)

        nameEditText = findViewById(R.id.addhospital_nameEditText)
        descriptionEditText = findViewById(R.id.addhospital_descriptionEditText)
        cityEditText = findViewById(R.id.addhospital_cityEditText)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map1) as SupportMapFragment
        mapFragment.getMapAsync{
            gMap = it
            setUpMap(gMap!!)
        }

        inputSearch = findViewById(R.id.inputSearch)
        inputSearch.setOnKeyListener(View.OnKeyListener { _, keyCode, keyEvent ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                getLocation(inputSearch.text.toString())
                inputSearch.text = ""
                return@OnKeyListener true
            }
            false
        })

    }

    private fun getLocation(input: String) {

        lateinit var location: String
        location = input

        var searchAddressList: List<Address>? = null

        try {
            val geocoder = Geocoder(this)
            searchAddressList = geocoder.getFromLocationName(location, 5)

        } catch (e: IOException) {
            e.printStackTrace()
        }

        if(!searchAddressList.isNullOrEmpty()) {

            val address = searchAddressList!![0]
            val latLng = LatLng(address.latitude, address.longitude)
            lat = address.latitude
            long = address.longitude
            cityEditText.text = address.locality.toString()
            gMap!!.addMarker(MarkerOptions().position(latLng).title(location))
            gMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        } else {
            Toast.makeText(this, "Couldn't find any location with name " + input, Toast.LENGTH_LONG).show()
        }
    }

    //This function is used to add Hospital to the Firestore
    private fun addHospital(myDB : FirebaseFirestore, name: String, description: String, city: String,
                    x_coord: Double, y_coord: Double) {

        val hospital = hashMapOf(
            "name" to name,
            "city" to city,
            "description" to description,
            "x_coord" to x_coord,
            "y_coord" to y_coord
        )
        myDB.collection("Hospitals")
            .add(hospital as Map<String, Any>)
            .addOnSuccessListener { documentReference ->
                Log.w(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error: ", e)
            }
    }

    override fun onMapReady(map: GoogleMap?) {
        gMap = map

        val hospitalPosition = LatLng(lat, long)
        val hospitalMarker: MarkerOptions = MarkerOptions().position(hospitalPosition).draggable(true)
        val zoomLevel = 15.0f

        gMap.let {
            it!!.addMarker(hospitalMarker)
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(hospitalPosition, zoomLevel))
        }
    }

    private fun setUpMap(map: GoogleMap) {
        map.uiSettings.setZoomControlsEnabled(true)
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        onMapReady(gMap)
    }

    //Function for submit button in AddHospitalActivity
    fun onClickSubmit(view: View) {
        //Checking if saving conditions are met (all fields are filled)
        addHospital_submitButton.setOnClickListener {
            if(nameEditText.text.toString().trim().isEmpty()) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alert_warningTitle)
                builder.setMessage(R.string.alert_emptyHospitalNameField)
                val dialog: AlertDialog = builder.create()
                dialog.show()

            } else if(descriptionEditText.text.toString().trim().isEmpty()){
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alert_warningTitle)
                builder.setMessage(R.string.alert_emptyHospitalDescriptionField)
                val dialog: AlertDialog = builder.create()
                dialog.show()

            } else if(cityEditText.text.toString().trim().isEmpty()){
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alert_warningTitle)
                builder.setMessage(R.string.alert_emptyHospitalCityField)
                val dialog: AlertDialog = builder.create()
                dialog.show()

            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alert_warningTitle)
                builder.setMessage(R.string.alert_saveHospitalMessage)

                builder.setPositiveButton(R.string.alert_positiveButton){dialog, which ->
                    hospitalName = nameEditText.text.toString().trim()
                    hospitalDescription = descriptionEditText.text.toString().trim()
                    hospitalCity = cityEditText.text.toString().trim()

                    val intent = Intent(this, DisplayHospitalsActivity::class.java)
                    startActivity(intent)

                    addHospital(myDB, hospitalName, hospitalDescription, hospitalCity, lat, long)
                    finish()
                }
                //Clearing input fields when user click negative button 
                builder.setNegativeButton(R.string.alert_negativeButton){dialog, which ->
                    nameEditText.text = ""
                    descriptionEditText.text = ""
                    cityEditText.text = ""
                }

                builder.setNeutralButton(R.string.alert_neutralButton){dialog, which ->
                    val intent = Intent(this, DisplayHospitalsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }
}


