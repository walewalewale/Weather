package com.decagon.weather.connection.rectrofit

import com.decagon.weather.model.ForecastDTO
import com.decagon.weather.model.WeatherDTO
import retrofit2.Call

class ApiCalls(private val param: Map<String, String>) : ICalls{
    private val retrofit = ApiClient.getClient()

    override fun getCurrentApi(): Call<WeatherDTO> {
        return retrofit.create(IApi::class.java).getCurrent(param)
    }

    override fun getForeCastApi(): Call<ForecastDTO> {
        return retrofit.create(IApi::class.java).getForeCast(param)
    }
}