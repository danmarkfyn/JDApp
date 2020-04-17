package com.example.jdapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.jdapp.model.FirestoreController
import com.google.firebase.firestore.FirebaseFirestore

class AddHospitalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hospital)

        val myDB = FirebaseFirestore.getInstance()
        addHospital(myDB,"nyt out","Nyt hospital", 1.1,3.2)

    }

    fun addHospital(myDB : FirebaseFirestore, name: String, description: String,
                    x_coord: Double, y_coord: Double) {

        val hospital = hashMapOf(
            "name" to name,
            "description" to description,
            "x_coord" to x_coord,
            "y_coord" to y_coord
        )

        FirestoreController.myDB.collection("Hospitals")
            .add(hospital as Map<String, Any>)
            .addOnSuccessListener { documentReference ->
                Log.w(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error: ", e)
            }
    }
}
