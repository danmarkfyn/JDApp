package com.example.jdapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity(){
    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

    }

    fun onClickLink (view: View) {
        val openURI = Intent(android.content.Intent.ACTION_VIEW)
        openURI.data = Uri.parse("https://www.linkedin.com/in/jakob-tøth")

        startActivity(openURI)
    }


}