package com.decagon.weather.repo

interface IForecastRepo {
    fun getCurrentOnline(forecastResult: ForecastResult)
    fun getCurrentOffline(forecastResult: ForecastResult)
}