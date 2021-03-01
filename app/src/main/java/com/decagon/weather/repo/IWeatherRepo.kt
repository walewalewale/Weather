package com.decagon.weather.repo

interface IWeatherRepo {
    fun getCurrentOnline(weatherResult: WeatherResult)
    fun getCurrentOffline(weatherResult: WeatherResult)
}