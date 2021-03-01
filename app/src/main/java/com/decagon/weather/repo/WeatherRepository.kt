package com.decagon.weather.repo

import com.decagon.weather.DateTimeUtil
import com.decagon.weather.connection.rectrofit.ApiCalls
import com.decagon.weather.data.room.WeatherDataBase
import com.decagon.weather.data.room.dao.IWeatherDao
import com.decagon.weather.data.room.entity.Weather
import com.decagon.weather.model.WeatherDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

class WeatherRepository(
    private val database: WeatherDataBase,
    param: Map<String, String>
) : IWeatherRepo, IWeatherDao {
    var apiCalls: ApiCalls = ApiCalls(param)
    override fun getCurrentOnline(weatherResult: WeatherResult) {
        try {
            apiCalls.getCurrentApi().enqueue(object : Callback<WeatherDTO> {
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        val weather = Weather(
                            responseBody.name!!,
                            DateTimeUtil.getDay(responseBody.dt!!.toLong()),
                            responseBody.weather!![0]!!.main!!,
                            responseBody.main!!.temp!!,
                            responseBody.main.temp_min!!,
                            responseBody.main.temp_max!!,
                            responseBody.coord!!.lon!!.toString(),
                            responseBody.coord.lat!!.toString()
                        )

                        insert(weather)
                        weatherResult.success(weather)
                    } else {
                        weatherResult.error(response.message())
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    weatherResult.error(t.message!!)
                }
            })
        } catch (ex: RuntimeException) {
            weatherResult.error("${ex.message}")
        }
    }

    override fun getCurrentOffline(weatherResult: WeatherResult) {
        GlobalScope.launch(Dispatchers.IO) {
            if(database.weatherDao().getAllWeathers().isNotEmpty()){
                weatherResult.success(database.weatherDao().getAllWeathers()[0]!!)
            }
        }
    }

    override fun insert(weather: Weather?) {
        GlobalScope.launch(Dispatchers.IO) {
            database.weatherDao().insert(weather)
        }
    }

    override fun update(
        name: String,
        state: String,
        date: String,
        currentTemp: Double,
        tempMin: Double,
        tempMax: Double
    ) {
    }
}