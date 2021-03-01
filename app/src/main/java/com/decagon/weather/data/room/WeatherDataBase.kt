package com.decagon.weather.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.decagon.weather.DateTimeUtil
import com.decagon.weather.data.room.dao.FavouriteDao
import com.decagon.weather.data.room.dao.ForecastDao
import com.decagon.weather.data.room.dao.WeatherDao
import com.decagon.weather.data.room.entity.Favourite
import com.decagon.weather.data.room.entity.Forecast
import com.decagon.weather.data.room.entity.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Weather::class, Forecast::class, Favourite::class], version = 3, exportSchema = true)
abstract class WeatherDataBase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun forecastDao(): ForecastDao
    abstract fun favouriteDao(): FavouriteDao

    private class WeatherDataBaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val weatherDao = database.weatherDao()
                    // Delete all content here.
                    weatherDao.deleteAll()

                    // Add sample weather.
                    val weather = Weather("Lagos", DateTimeUtil.getDay(System.currentTimeMillis()), "Rainy", 20.0, 19.5, 21.1, "3.4823", "6.4502")
                    weatherDao.insert(weather)

                    val forecastDao = database.forecastDao()
                    // Delete all content here.
                    forecastDao.deleteAll()

                    // Add sample forcaste.
                    val forecast0 = Forecast("Lagos", DateTimeUtil.getDay(System.currentTimeMillis()), "Rainy", 21.0, 19.5, 22.1, "3.4823", "6.4502")
                    forecastDao.insert(forecast0)

                    val forecast1 = Forecast("Abuja", DateTimeUtil.getDay(System.currentTimeMillis()), "Clouds", 22.0, 20.5, 20.1, "3.4823", "6.4502")
                    forecastDao.insert(forecast1)

                    val forecast2 = Forecast("Benin", DateTimeUtil.getDay(System.currentTimeMillis()), "Rainy", 23.0, 19.5, 20.2, "3.4823", "6.4502")
                    forecastDao.insert(forecast2)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `forecast_table` (`id` INTEGER NOT NULL DEFAULT 0`, name` TEXT, `date` INTEGER, `state` TEXT, `currentTemp` REAL, `tempMin` REAL, `tempMax` REAL, `lon` TEXT, `lat` TEXT, " +
                        "PRIMARY KEY(`id`))")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `favourite_table` (`name` TEXT NOT NULL DEFAULT Lagos, `lon` TEXT, `lat` TEXT, " +
                        "PRIMARY KEY(`name`))")
            }
        }

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WeatherDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDataBase::class.java,
                    "weather_database"
                )
                    .addCallback(WeatherDataBaseCallback(scope))
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}