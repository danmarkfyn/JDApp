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

        // get layout items
        val titleText = rowView.findViewById(R.id.adapter_title) as TextView
        val subtitleText = rowView.findViewById(R.id.adapter_description) as TextView

        // get data items
        val title = data[position].number.toString() +". " + data[position].name
        val description = data[position].description


        // set layout item values = data item values
        titleText.text = title
        subtitleText.text = description

        Log.d(ContentValues.TAG, "Custom Adapter data values: " + title + " : " + description)

        return rowView
    }
}