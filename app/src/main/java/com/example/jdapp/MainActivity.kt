package com.example.jdapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    // TODO layout naming convetion  https://jeroenmols.com/blog/2016/03/07/resourcenaming/
    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun onClickHospital (view: View) {
        val intent = Intent(this, HospitalsActivity::class.java)
        startActivity(intent)
    }

}
