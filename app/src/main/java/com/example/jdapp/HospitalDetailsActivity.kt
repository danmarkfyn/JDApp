package com.example.jdapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class HospitalDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_details)

        val extras = intent.extras ?: return
        val hospitalSelected = extras.getString("HospitalName")

        val textView = findViewById<TextView>(R.id.textView2)
        textView.text = hospitalSelected

    }
}
