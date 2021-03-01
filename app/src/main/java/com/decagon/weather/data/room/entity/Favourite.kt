package com.decagon.weather.data.room.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_table")
class Favourite(
    name: String,
    lon: String,
    lat: String
) {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    var name : String ?= name

    var lon : String ?= lon
    var lat : String ?= lat
}