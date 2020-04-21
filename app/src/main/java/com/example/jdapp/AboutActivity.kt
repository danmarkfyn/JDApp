package com.example.jdapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


/**
 * activity for showing information about the programmers behind the app
 */
class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

    }

    fun onClickLink1(view: View) {
        val openURI = Intent(Intent.ACTION_VIEW)
        openURI.data = Uri.parse("https://www.linkedin.com/in/jakob-tøth")

        startActivity(openURI)
    }

    fun onClickLink2(view: View) {
        val openURI = Intent(Intent.ACTION_VIEW)
        openURI.data = Uri.parse("https://www.linkedin.com/in/dariusz-orasinski-689603177/")
        startActivity(openURI)
    }
}