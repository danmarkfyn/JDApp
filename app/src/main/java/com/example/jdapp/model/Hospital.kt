package com.example.jdapp.model

/**
 * Data object for a Hospital
 * @param name name of Hospital. A String
 * @param city city where the hospital is located. A String
 * @Param description a short description attached to the hospital. A String
 * @param xCoord the x coordinate for the geo location of the hospital. A Double
 * @param xCoord the y coordinate for the geo location of the hospital. A Double
 * @param temp the temperature at the location. A String
 */

data class Hospital(
    val name: String,
    val city: String,
    val description: String,
    val xCoord: Double,
    val yCoord: Double,
    val temp: String
) {
}