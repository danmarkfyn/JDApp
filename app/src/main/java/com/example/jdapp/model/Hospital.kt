package com.example.jdapp.model

import com.google.firebase.firestore.GeoPoint

data class Hospital(
                    val name: String,
                    val city: String,
                    val description: String,
                    val xCoord: Double,
                    val yCoord: Double,
                    val temp: String) {
}