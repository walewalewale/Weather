package com.decagon.weather.connection.rectrofit

import com.decagon.weather.model.ForecastDTO
import com.decagon.weather.model.WeatherDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface IApi{

    @GET("weather")
    fun getCurrent(@QueryMap queries : Map<String, String>): Call<WeatherDTO>

    @GET("onecall")
    fun getForeCast(@QueryMap queries : Map<String, String>): Call<ForecastDTO>

}