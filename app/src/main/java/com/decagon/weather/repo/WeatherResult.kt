package com.decagon.weather.repo

import com.decagon.weather.data.room.entity.Weather

interface WeatherResult {
    fun success(value: Weather)
    fun error(value:String)
}