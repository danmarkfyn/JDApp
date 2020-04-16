package com.example.jdapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewParent
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

class HospitalsActivity : AppCompatActivity() {

    var hospitals = arrayOf("Odense Universites Hospital", "Svendborg Sygehus")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospitals)

        val listOfHospitals = findViewById<ListView>(R.id.listOfHospitals)

        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, hospitals)
        listOfHospitals.adapter = arrayAdapter

        listOfHospitals.setOnItemClickListener { adapterview, view, i, id ->

            val selectedHospital = arrayAdapter.getItem(i)
            val intent = Intent(this, HospitalDetailsActivity::class.java)
            intent.putExtra("HospitalName", selectedHospital)
            startActivity(intent)
        }

    }


}
