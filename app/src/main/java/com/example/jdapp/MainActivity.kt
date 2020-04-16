package com.example.jdapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myDB = FirebaseFirestore.getInstance()

        val h1 = hashMapOf(
            "name" to "Svendborg Sygehus",
            "description" to "Hospital Located in Svendborg"
            )
        myDB.collection("Hospitals")
            .add(h1 as Map<String, Any>)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")

            }
            .addOnFailureListener { e ->
                println("Error. Could not add document$e")
            }



    }


}
