package com.decagon.weather.repo

import com.decagon.weather.data.room.entity.Favourite

interface FavouriteResult {
    fun success(value: List<Favourite>?)
    fun error(value:String)
}