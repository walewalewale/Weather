package com.decagon.weather.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.decagon.weather.data.room.entity.Weather

@Dao
interface WeatherDao {

    @Insert(onConflict = REPLACE)
    fun insert(weather: Weather?)

    @Query("DELETE FROM weather_table")
    fun deleteAll()

    @Query("DELETE FROM weather_table WHERE name = :name")
    fun deleteSingle(name: String)

    @Query("UPDATE weather_table SET state = :state, date = :date, currentTemp = :currentTemp, tempMin = :tempMin, tempMax =:tempMax WHERE name = :name")
    fun updateWeather(name:String, state: String, date: String, currentTemp: Double, tempMin: Double, tempMax: Double)

    @Query("SELECT * from weather_table ORDER BY name ASC")
    fun getAllWeathers(): List<Weather?>
}