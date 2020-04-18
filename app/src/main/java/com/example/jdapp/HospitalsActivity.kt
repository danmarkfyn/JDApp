package com.example.jdapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.jdapp.model.Hospital
import com.google.firebase.firestore.FirebaseFirestore

class HospitalsActivity : AppCompatActivity() {

    private val hospitals = ArrayList<Hospital>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospitals)

        val listOfHospitals = findViewById<ListView>(R.id.listOfHospitals)
        val myDB = FirebaseFirestore.getInstance()

        getHospitals(myDB,listOfHospitals)
    }
    fun onClickHospital (view: View) {
        val intent = Intent(this, AddHospitalActivity::class.java)
        startActivity(intent)
    }

// TODO fun explanation
private fun getHospitals(myDB : FirebaseFirestore, listOfHospitals : ListView){

        myDB.collection("Hospitals")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val h = Hospital(
                        document.getString("name").toString(),
                        document.getString("description").toString(),
                        document.get("x_coord").toString().toDouble(),
                        document.get("y_coord").toString().toDouble()
                    )
                    hospitals.add(h)
                    Log.d(ContentValues.TAG, "Retrieved from DB${h}")
                }
                Log.d(ContentValues.TAG, "Size of arraylist " + hospitals.size)


               val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, hospitals)

                listOfHospitals.adapter = arrayAdapter

                listOfHospitals.FixedViewInfo()

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
}
