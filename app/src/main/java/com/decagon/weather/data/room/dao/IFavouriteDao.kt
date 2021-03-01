package com.decagon.weather.data.room.dao

import com.decagon.weather.data.room.entity.Favourite

interface IFavouriteDao {
    fun insert(favourite: Favourite)
}