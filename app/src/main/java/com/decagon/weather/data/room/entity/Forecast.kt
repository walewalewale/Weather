package com.decagon.weather.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_table")
class Forecast(
    name: String,
    date: String,
    state: String,
    currentTemp: Double,
    tempMin: Double,
    tempMax: Double,
    lon: String,
    lat: String
) {

    @PrimaryKey(autoGenerate = true) var id: Int = 0

    var name : String ?= name
    var date:String?= date
    var state: String?= state
    var currentTemp: Double?= currentTemp
    var tempMin: Double?= tempMin
    var tempMax: Double?= tempMax
    var lon : String ?= lon
    var lat : String ?= lat

}