package com.example.jdapp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jdapp.database.FirestoreController
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbController = FirestoreController

        //dbController.addHospital("Skejby","A danish institution", 66.8,78.3)
        //dbController.getHospital()
        var p = dbController.getHospitals()

        Toast.makeText(applicationContext,"this is toast message " +p,Toast.LENGTH_SHORT).show()


    }
}
