package com.decagon.weather.data.room.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
class Weather(
    name: String,
    date: String,
    state: String,
    currentTemp: Double,
    tempMin: Double,
    tempMax: Double,
    lon: String,
    lat: String
) {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    var name : String ?= name

    var date:String?= date
    var state: String?= state
    var currentTemp: Double?= currentTemp
    var tempMin: Double?= tempMin
    var tempMax: Double?= tempMax
    var lon : String ?= lon
    var lat : String ?= lat

}