package com.example.jdapp.database

import android.content.ContentValues.TAG
import android.util.Log
import kotlin.text.*
import com.google.firebase.firestore.FirebaseFirestore


// Singleton to avoid several instantiations of DB access
object FirestoreController {



    private val hospitals = ArrayList<Hospital>()
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
    // TODO need to return a string
    // TODO Also https://stackoverflow.com/questions/39798269/return-from-lambdas-or-kotlin-return-is-not-allowed-here
    fun getHospitals(): ArrayList<Hospital>{



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
                    Log.d(TAG, "Size of arraylist " + hospitals.size)
                    Log.d(TAG, "Retrieved from DB${h}")
                    }
                }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error: ", e)
                    }
                return hospitals
            }

    fun getHospital(){

        myDB.collection("Hospitals")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "Retrieved from DB${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error: ", e)
            }
        }
    }


