package com.decagon.weather.model

data class ForecastDTO(
    val current: Current?,
    val daily: List<Daily?>?,
    val lat: Any?,
    val lon: Any?,
    val timezone: String?,
    val timezone_offset: Any?
) {
    data class Current(
        val clouds: Any?,
        val dew_point: Any?,
        val dt: Any?,
        val feels_like: Double?,
        val humidity: Any?,
        val pressure: Any?,
        val sunrise: Any?,
        val sunset: Any?,
        val temp: Any?,
        val uvi: Any?,
        val visibility: Any?,
        val weather: List<Weather?>?,
        val wind_deg: Any?,
        val wind_speed: Any?
    ) {
        data class Weather(
            val description: Any?,
            val icon: Any?,
            val id: Any?,
            val main: Any?
        )
    }

    data class Daily(
        val clouds: Any?,
        val dew_point: Any?,
        val dt: Double?,
        val feels_like: FeelsLike?,
        val humidity: Any?,
        val pop: Any?,
        val pressure: Any?,
        val rain: Any?,
        val sunrise: Any?,
        val sunset: Any?,
        val temp: Temp?,
        val uvi: Any?,
        val weather: List<Weather?>?,
        val wind_deg: Any?,
        val wind_speed: Any?
    ) {
        data class FeelsLike(
            val day: Any?,
            val eve: Any?,
            val morn: Any?,
            val night: Any?
        )

        data class Temp(
            val day: Any?,
            val eve: Any?,
            val max: Any?,
            val min: Any?,
            val morn: Any?,
            val night: Any?
        )

        data class Weather(
            val description: Any?,
            val icon: Any?,
            val id: Any?,
            val main: Any?
        )
    }
}