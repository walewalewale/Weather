package com.decagon.weather.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.decagon.weather.data.room.entity.Forecast
import com.decagon.weather.data.room.entity.Weather

@Dao
interface ForecastDao {

    @Insert(onConflict = IGNORE)
    fun insert(forecast: Forecast?)

    @Query("DELETE FROM forecast_table")
    fun deleteAll()

    @Query("DELETE FROM forecast_table WHERE name = :name")
    fun deleteSingle(name: String)

    @Query("UPDATE forecast_table SET state = :state, date = :date, currentTemp = :currentTemp, tempMin = :tempMin, tempMax =:tempMax WHERE name = :name")
    fun updateForecast(name:String, state: String, date: String, currentTemp: Double, tempMin: Double, tempMax: Double)

    @Query("SELECT * from forecast_table ORDER BY name ASC")
    fun getAllForecast(): List<Forecast>
}