package com.example.jdapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import io.opencensus.resource.Resource
import java.util.*

class SettingsActivity : AppCompatActivity() {

    lateinit var changeLanguageButton: Button
    private var currentLanguage: String = "English"
    private lateinit var myLocale: Locale

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        changeLanguageButton = findViewById(R.id.changeLanguageButton)
        changeLanguageButton.setOnClickListener{
            languageOptions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun languageOptions() {

        val langOptions = arrayOf("English", "Danish", "Polish")
        val aBuilder = AlertDialog.Builder(this)
        aBuilder.setTitle("Choose Language")
        aBuilder.setSingleChoiceItems(langOptions, -1) {dialog, which ->
            if(which == 0) {
                setLanguage("english")
                recreate()
            } else if (which == 1) {
                setLanguage("danish")
                recreate()
            } else if (which == 2) {
                setLanguage("polish")
                recreate()
            }
            dialog.dismiss()
        }
        val aDialog = aBuilder.create()
        aDialog.show()

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setLanguage(Lang: String) {
        if(!Lang.equals(currentLanguage)) {

            var context = applicationContext

            val locale = Locale(Lang)
            Locale.setDefault(locale)
            val config = context.resources.configuration
            setSystemLocale(config ,locale)


            val refresh = Intent(this, SettingsActivity::class.java)
            refresh.putExtra(currentLanguage, Lang)
            startActivity(refresh)


        } else {
            Toast.makeText(this, "This is the current language ", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun setSystemLocale(config: Configuration, locale: Locale) {
        config.setLocale(locale)
    }
}
