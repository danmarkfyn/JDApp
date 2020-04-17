package com.example.jdapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.view.View
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_hospital.*

class AddHospitalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hospital)

        val myDB = FirebaseFirestore.getInstance()
        addHospital(myDB,"nyt out","Nyt hospital", 1.1,3.2)

        val nameEditText = findViewById<TextView>(R.id.addhospital_nameEditText)
        val hospitalName = nameEditText.text
        //val textView = findViewById<TextView>(R.id.textView2)
        //textView.text = hospitalSelected

    }

    fun addHospital(myDB : FirebaseFirestore, name: String, description: String,
                    x_coord: Double, y_coord: Double) {

        val hospital = hashMapOf(
            "name" to name,
            "description" to description,
            "x_coord" to x_coord,
            "y_coord" to y_coord
        )

        myDB.collection("Hospitals")
            .add(hospital as Map<String, Any>)
            .addOnSuccessListener { documentReference ->
                Log.w(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error: ", e)
            }
    }

    //Function for submit button in AddHospitalActivity
    fun onClickSubmit(view: View) {

    }
}
