package com.decagon.weather.data.room.dao

import com.decagon.weather.data.room.entity.Forecast

interface IForecastDao {
    fun insert(forecast: ArrayList<Forecast>)

    fun update(name:String, state: String, date: String, currentTemp: Double, tempMin: Double, tempMax: Double)
}