package com.decagon.weather

import android.app.Application
import com.decagon.weather.data.room.WeatherDataBase
import com.decagon.weather.di.AppComponent
import com.decagon.weather.di.DaggerAppComponent
import com.decagon.weather.repo.FavouriteRepository
import com.decagon.weather.repo.ForecastRepository
import com.decagon.weather.repo.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WeatherApplication : Application() {
    private val appComponent: AppComponent
    fun getAppComponent(): AppComponent {
        return appComponent
    }

    init {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }

    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { WeatherDataBase.getDatabase(this, applicationScope) }

    fun getWeatherRepository(param : Map<String, String>): WeatherRepository {
        return WeatherRepository(database, param)
    }

    fun getForecastRepository(param : Map<String, String>): ForecastRepository {
        return ForecastRepository(database, param)
    }

    fun getFavouriteRepository(param : Map<String, String>?): FavouriteRepository {
        return FavouriteRepository(database, param)
    }
}