package com.decagon.weather.repo

import com.decagon.weather.DateTimeUtil
import com.decagon.weather.connection.rectrofit.ApiCalls
import com.decagon.weather.data.room.WeatherDataBase
import com.decagon.weather.data.room.dao.IForecastDao
import com.decagon.weather.data.room.entity.Forecast
import com.decagon.weather.model.ForecastDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

class ForecastRepository(
    private val database: WeatherDataBase,
    param: Map<String, String>
) : IForecastRepo, IForecastDao {
    var apiCalls: ApiCalls = ApiCalls(param)
    override fun getCurrentOnline(forecastResult: ForecastResult) {
        try {
            apiCalls.getForeCastApi().enqueue(object : Callback<ForecastDTO> {
                override fun onResponse(call: Call<ForecastDTO>, response: Response<ForecastDTO>) {
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        val name = responseBody.timezone!!.split("/")[1]
                        val lon = responseBody.lon
                        val lat = responseBody.lat
                        val forecastList = ArrayList<Forecast>()
                        for (i in responseBody.daily!!){
                            val weather = i!!.weather!![0]!!;
                            val temp = i.temp
                            val feelslike = i.feels_like
                            val forecast = Forecast(
                                name,
                                DateTimeUtil.getDay(i.dt!!.toLong()),
                                weather.main!!.toString(),
//                                temp!!.day!!.toString().toDouble(),//Too identical
                                feelslike!!.day!!.toString().toDouble(), //A little difference
                                temp!!.min!!.toString().toDouble(),
                                temp.max!!.toString().toDouble(),
                                lon.toString(),
                                lat.toString()
                            )
                            forecastList.add(forecast)
                        }

                        insert(forecastList)
                        forecastResult.success(forecastList)
                    } else {
                        forecastResult.error(response.message())
                    }
                }

                override fun onFailure(call: Call<ForecastDTO>, t: Throwable) {
                    forecastResult.error(t.message!!)
                }
            })
        } catch (ex: RuntimeException) {
            forecastResult.error("${ex.message}")
        }
    }

    override fun getCurrentOffline(forecastResult: ForecastResult) {
        GlobalScope.launch(Dispatchers.IO) {
            forecastResult.success(database.forecastDao().getAllForecast())
        }
    }

    override fun insert(forecast: ArrayList<Forecast>) {
        GlobalScope.launch(Dispatchers.IO) {
            database.forecastDao().deleteAll()
            forecast.forEach { t -> database.forecastDao().insert(t) }
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