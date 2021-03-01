package com.decagon.weather.data.room.dao

import com.decagon.weather.data.room.entity.Weather

interface IWeatherDao {
    fun insert(weather: Weather?)

    fun update(name:String, state: String, date: String, currentTemp: Double, tempMin: Double, tempMax: Double)
}