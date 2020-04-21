package com.example.jdapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import com.example.jdapp.adapters.FilterAdapter
import com.example.jdapp.model.Hospital
import com.example.jdapp.services.WeatherService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_displayhospitals.*

class DisplayHospitalsActivity : AppCompatActivity() {

    private val hospitals = ArrayList<Hospital>()
    private val types = ArrayList<String>()
    private val myDB = FirebaseFirestore.getInstance()
    private var selected = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_displayhospitals)

        hospitals.clear()
        types.add("No Filters")

        var listOfHospitals = findViewById<ListView>(R.id.listOfHospitals)
        val filterSpinner: Spinner = findViewById(R.id.filterSpinner)


        // sets up ArrayAdapter to fill spinner with array entities
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        filterSpinner.adapter = arrayAdapter

        // listens to interactions with spinner
        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selected = ""
                Log.d(
                    ContentValues.TAG,
                    "Spinner Not Selected " + filterSpinner.selectedItem.toString()
                )
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                hospitals.clear()
                selected = filterSpinner.selectedItem.toString()

                getHospitals(myDB, listOfHospitals)
                Log.d(
                    ContentValues.TAG,
                    "Spinner Selected " + filterSpinner.selectedItem.toString()
                )
            }
        }
    }

    // ensures that the ListView reflect changes made in the cloud DB
    override fun onRestart() {
        super.onRestart()
        hospitals.clear()
        getHospitals(myDB, listOfHospitals)
        Log.d(ContentValues.TAG, this.localClassName + " is restarted")
    }

    private fun getHospitals(myDB: FirebaseFirestore, listOfHospitals: ListView) {
        Log.d(ContentValues.TAG, "Getting Entities from DB")



        myDB.collection("Hospitals")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {


                    // get values from DB
                    /*
                    val name = document.getString("name").toString()
                    val city = document.getString("city").toString()
                    val description = document.getString("description").toString()
                    val xCoord : Double = document.getString("x_coord").toString().toDouble()
                    val yCoord : Double = document.getString("y_coord").toString().toDouble()

                     */
                    val city = document.getString("city").toString()
                    val temp = WeatherService(city).execute().get()


                    // TODO implement GeoPoint https://stackoverflow.com/questions/53799346/how-to-convert-geopoint-in-firestore-to-latlng

                    val h = Hospital(
                        city,
                        document.getString("city").toString(),
                        document.getString("description").toString(),
                        document.get("x_coord").toString().toDouble(),
                        document.get("y_coord").toString().toDouble(),
                        temp
                    )

                    // reads cities & removes formatting to allow for filtering
                    var type = document.getString("city").toString().toLowerCase().trim()


                    // dynamically reads the cities from the entities on the database for use in the spinner
                    if (document.getString("city") != null && !types.contains(type)) {
                        types.add(type.toLowerCase().trim())
                        Log.d(
                            ContentValues.TAG,
                            "City " + type + " found on DB and was added as a filter option"
                        )
                    }

                    // shows all loaded cities if no filters applied (applied "No Filters" filter)
                    if (selected == "No Filters") {
                        hospitals.add(h)
                    } else {
                        if (selected == h.city.trim().toLowerCase() && selected != "No Filters") {
                            hospitals.add(h)
                            Log.d(ContentValues.TAG, "Added hospital to list according to filers ")
                        }
                    }

                    Log.d(ContentValues.TAG, "Retrieved from DB${h}")
                }
                Log.d(ContentValues.TAG, "Size of arraylist " + hospitals.size)

                val filterAdapter =
                    FilterAdapter(this, hospitals)

                listOfHospitals.adapter = filterAdapter

                listOfHospitals.setOnItemClickListener { adapterview, view, i, id ->

                    val selectedHospital = filterAdapter.getItem(i)
                    //Values passed to the DisplayHospitalActvity
                    val selectedHospitalName = selectedHospital?.name.toString()
                    val selectedHospitalDescription = selectedHospital?.description.toString()
                    val selectedHospitalCity = selectedHospital?.city.toString()
                    val selectedHospitalLat = selectedHospital?.xCoord.toString()
                    val selectedHospitalLong = selectedHospital?.yCoord.toString()

                    //Intent for HospitalDetailsActivity
                    val intent = Intent(this, HospitalDetailsActivity::class.java)
                    intent.putExtra("HospitalName", selectedHospitalName)
                    intent.putExtra("HospitalDescription", selectedHospitalDescription)
                    intent.putExtra("HospitalCity", selectedHospitalCity)
                    intent.putExtra("HospitalLat", selectedHospitalLat)
                    intent.putExtra("HospitalLong", selectedHospitalLong)
                    startActivity(intent)
                }
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error: ", e)
            }
    }

    fun onClickAddHospital(view: View) {
        val intent = Intent(this, AddHospitalActivity::class.java)
        startActivity(intent)
    }
}

