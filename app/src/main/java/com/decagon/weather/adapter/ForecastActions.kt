package com.decagon.weather.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.decagon.weather.data.room.entity.Forecast

interface ForecastActions {
    fun renderItem(
        forecast: Forecast,
        itemParentLayout: ConstraintLayout?,
        date: TextView?,
        weatherIcon: ImageView?,
        degree: TextView?,
    )
}