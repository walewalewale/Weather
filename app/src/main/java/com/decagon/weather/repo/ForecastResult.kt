package com.decagon.weather.repo

import com.decagon.weather.data.room.entity.Forecast

interface ForecastResult {
    fun success(value: List<Forecast>)
    fun error(value:String)
}