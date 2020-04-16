package com.example.jdapp.database

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


// TODO make singleton to avoid several controllers
class FirestoreController() {

    val myDB = FirebaseFirestore.getInstance()

    fun addHospital(name: String, description: String,
                    x_coord: Double, y_coord: Double){

        val hospital = hashMapOf(
            "name" to name,
            "description" to description,
            "x_coord" to x_coord,
            "y_coord" to y_coord
        )

    myDB.collection("Hospitals")
        .add(hospital as Map<String, Any>)
        .addOnSuccessListener { documentReference ->
            Log.w(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w(TAG, "Error: ", e)
        }
}
    fun getHospital(){
        myDB.collection("Hospitals")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                }
            }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error: ", e)
                    }
            }
    }


