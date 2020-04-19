package com.example.jdapp.adapters

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.jdapp.R
import com.example.jdapp.model.Hospital


// custom adapter for filtered hospital ListView
class FilterAdapter(private val context: Activity, private val data: ArrayList<Hospital>)
    : ArrayAdapter<Hospital>(context,
    R.layout.filter_list, data) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.filter_list, null, true)

        // index N.
        val number = (position + 1).toString()

        // get layout items
        val titleText = rowView.findViewById(R.id.adapter_title) as TextView
        val descriptionText = rowView.findViewById(R.id.adapter_description) as TextView
        val cityText = rowView.findViewById(R.id.adapter_city) as TextView

        // set text of titleText
        val title = number + ". " + data[position].name
        titleText.text = title

        // set text of descriptionText
        val description = data[position].description
        descriptionText.text = description

        // set text of cityText
        val city = ": " + data[position].city
        cityText.append(city)


        Log.d(ContentValues.TAG, "Custom Adapter data values: " + title + " : " + description)

        return rowView
    }
}