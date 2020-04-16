package com.example.jdapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
    }

    fun onClickHospital (view: View) {
        val intent = Intent(this, HospitalsActivity::class.java)
        startActivity(intent)
    }

}
