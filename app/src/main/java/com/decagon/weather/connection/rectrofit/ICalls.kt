package com.decagon.weather.connection.rectrofit

import com.decagon.weather.model.ForecastDTO
import com.decagon.weather.model.WeatherDTO
import retrofit2.Call

interface ICalls {
    fun getCurrentApi() : Call<WeatherDTO>
    fun getForeCastApi() : Call<ForecastDTO>
}