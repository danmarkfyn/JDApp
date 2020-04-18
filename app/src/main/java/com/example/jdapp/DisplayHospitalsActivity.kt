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
import com.example.jdapp.model.Hospital
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_displayhospitals.*

class DisplayHospitalsActivity : AppCompatActivity() {

    private val hospitals = ArrayList<Hospital>()
    private val types = ArrayList<String>()
    private val myDB = FirebaseFirestore.getInstance()
    private var selected = ""


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_displayhospitals)

        types.add("No Filters")

        var listOfHospitals = findViewById<ListView>(R.id.listOfHospitals)
        val filterSpinner: Spinner = findViewById(R.id.filterSpinner)
        getHospitals(myDB, listOfHospitals)

        // sets up ArrayAdapter to fill spinner with array entities
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            filterSpinner.adapter = arrayAdapter

            // listens to interactions with spinner
            filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selected = ""
                Log.d(ContentValues.TAG, "Spinner Not Selected " + filterSpinner.selectedItem.toString())
            }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    hospitals.clear()
                    selected = filterSpinner.selectedItem.toString()

                getHospitals(myDB, listOfHospitals)
                Log.d(ContentValues.TAG, "Spinner Selected " + filterSpinner.selectedItem.toString())
            }
        }
    }

    // ensures that the ListView reflect changes made in the cloud DB
    override fun onRestart() {
        super.onRestart()
        hospitals.clear()
        getHospitals(myDB, listOfHospitals)
        Log.d(ContentValues.TAG, this.localClassName+ " is restarted")
    }

private fun getHospitals(myDB : FirebaseFirestore, listOfHospitals : ListView) {

        myDB.collection("Hospitals")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val h = Hospital(
                        hospitals.size + 1,
                        document.getString("name").toString(),
                        document.getString("city").toString(),
                        document.getString("description").toString(),
                        document.get("x_coord").toString().toDouble(),
                        document.get("y_coord").toString().toDouble()
                    )

                    // reads cities & removes formatting to allow for filtering
                    var type = document.getString("city").toString().toLowerCase().trim()


                    // dynamically reads the cities from the entities on the database for use in the spinner
                    if (document.getString("city") != null && !types.contains(type)) {
                        types.add(type.toLowerCase().trim())
                        Log.d(ContentValues.TAG, "City " + type + " found on DB and was added as a filter option")
                    }

                    // shows all loaded cities if no filters applied (applied "No Filters" filter)
                    if(selected == "No Filters"){
                        hospitals.add(h)
                    }else{
                        if(selected == h.city.trim().toLowerCase()){
                            hospitals.add(h)
                            Log.d(ContentValues.TAG, "Added hospital to list according to filers ")
                        }
                    }

                    Log.d(ContentValues.TAG, "Retrieved from DB${h}")
                }
                Log.d(ContentValues.TAG, "Size of arraylist " + hospitals.size)


                val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, hospitals)
                listOfHospitals.adapter = arrayAdapter
                listOfHospitals.setOnItemClickListener { adapterview, view, i, id ->

                    val selectedHospital = arrayAdapter.getItem(i)
                    val intent = Intent(this, HospitalDetailsActivity::class.java)

                    // TODO pass selected name, description & x,y
                    // TODO show distance from current pos
                    intent.putExtra("HospitalName", selectedHospital.toString())
                    startActivity(intent)
                }
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error: ", e)
            }
    }
    fun onClickAddHospital (view : View) {
        val intent = Intent(this, AddHospitalActivity::class.java)
        startActivity(intent)
    }
}

