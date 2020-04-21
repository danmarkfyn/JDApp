package com.example.jdapp

import android.content.ContentValues
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
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

/**
 * Parameters for AddHospitalActivity
 * @param hospitalName: Defines hospital name given by user
 * @param hospitalDescription: Defines hospital description given by user
 * @param hospitalCity: Defines location city for the hospital
 * @param lat: Location latititude for the hospital
 * @param long: Location logitude for the hospital
 */

class AddHospitalActivity : AppCompatActivity(), OnMapReadyCallback {
    //Init variables
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

    //onCreate method for AddHospital Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hospital)

        nameEditText = findViewById(R.id.addhospital_nameEditText)
        descriptionEditText = findViewById(R.id.addhospital_descriptionEditText)
        cityEditText = findViewById(R.id.addhospital_cityEditText)

        //Creates map fragement and calling setUpMap() for settup for the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map1) as SupportMapFragment
        mapFragment.getMapAsync{
            gMap = it
            setUpMap(gMap!!)
        }
        //A keyListener for the inputSearch edit text. Used to read user input and parsing it to the getLocation() function
        inputSearch = findViewById(R.id.inputSearch)
        inputSearch.setOnKeyListener(View.OnKeyListener { _, keyCode, keyEvent ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                var userInput = inputSearch.text.toString()
                getLocation(userInput)
                inputSearch.text = ""
                return@OnKeyListener true
            }
            false
        })
    }
    //Using geocoding to get the location based on the user input
    private fun getLocation(input: String) {
        lateinit var location: String
        location = input

        var searchAddressList: List<Address>? = null

        try {
            val geocoder = Geocoder(this)
            searchAddressList = geocoder.getFromLocationName(location, 1)

            if(searchAddressList.isNotEmpty()) {

                val address = searchAddressList!![0]
                val latLng = LatLng(address.latitude, address.longitude)
                lat = address.latitude
                long = address.longitude
                if(!address.locality.isNullOrEmpty()) {
                    cityEditText.text = address.locality.toString()
                } else {
                    Toast.makeText(this, "Couldn't find any city from " + input, Toast.LENGTH_LONG).show()
                }

                gMap!!.addMarker(MarkerOptions().position(latLng).title(location))
                gMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            } else {
                Toast.makeText(this, "Couldn't find any location with name " + input, Toast.LENGTH_LONG).show()
                cityEditText.text = ""
            }

        } catch (e: IOException) {
            e.printStackTrace()
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
    //Called when the map is ready
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

    //Function used to setting up the map
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
                //When conditons are met
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alert_warningTitle)
                builder.setMessage(R.string.alert_saveHospitalMessage)

                builder.setPositiveButton(R.string.alert_positiveButton){dialog, which ->
                    hospitalName = nameEditText.text.toString().trim()
                    hospitalDescription = descriptionEditText.text.toString().trim()
                    hospitalCity = cityEditText.text.toString().trim()

                    val intent = Intent(this, DisplayHospitalsActivity::class.java)
                    startActivity(intent)
                    //Saving hospital data by addHospital() function
                    addHospital(myDB, hospitalName, hospitalDescription, hospitalCity, lat, long)
                    finish()
                }
                //Clearing input fields when user click negative button 
                builder.setNegativeButton(R.string.alert_negativeButton){dialog, which ->
                    nameEditText.text = ""
                    descriptionEditText.text = ""
                    cityEditText.text = ""
                }
                //Sending user back to the DisplayHospitalActivity when cancel
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


