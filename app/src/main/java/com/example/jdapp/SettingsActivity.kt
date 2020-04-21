package com.example.jdapp

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import java.util.*

class SettingsActivity : AppCompatActivity() {

    lateinit var changeLanguageButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        loadLocate()

        changeLanguageButton = findViewById(R.id.changeLanguageButton)
        changeLanguageButton.setOnClickListener{
            languageOptions()
        }
    }

    private fun languageOptions() {
        //val langOptions: Array<> = resources.getStringArray(R.array.languageOptions)
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

    private fun setLanguage(Lang: String){
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Language", Lang)
        editor.apply()

    }

    private fun loadLocate() {
        val sharedPreferecens = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferecens.getString("My_Language", "")
        setLanguage(language!!)
    }




}
