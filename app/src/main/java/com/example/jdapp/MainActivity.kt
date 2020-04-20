package com.example.jdapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.jdapp.services.Weather
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    // TODO layout naming convetion  https://jeroenmols.com/blog/2016/03/07/resourcenaming/
    // TODO in general https://kotlinlang.org/docs/reference/coding-conventions.html
    // TODO fun explianations to all fun(ctions)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wer = Weather ("Den Bosch")
        wer.execute()
        setupFBMessaging()
    }

    fun onClickHospital(view: View) {
        val intent = Intent(this, DisplayHospitalsActivity::class.java)
        startActivity(intent)
    }

    fun onClickAbout(view: View) {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    /**
     * used to set up FirebaseMessaging and subscribe to entityAdded
     */
    private fun setupFBMessaging() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseMessaging.getInstance().subscribeToTopic("entityAdded")
            .addOnCompleteListener { task ->
                val token = FirebaseInstanceId.getInstance();
                Log.d(ContentValues.TAG, "Token used for subscription" + token)
                if (!task.isSuccessful) {

                    Log.d(ContentValues.TAG, "Failed to subscribe")
                }
            }
    }
}
