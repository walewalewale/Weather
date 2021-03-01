package com.decagon.weather.repo

import com.decagon.weather.data.room.WeatherDataBase
import com.decagon.weather.data.room.dao.IFavouriteDao
import com.decagon.weather.data.room.entity.Favourite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavouriteRepository(
    private val database: WeatherDataBase, private val favourite:Map<String, String>?) : IFavouriteRepo, IFavouriteDao {

    override fun getCurrentOffline(favouriteResult: FavouriteResult) {
        GlobalScope.launch(Dispatchers.IO) {
            favouriteResult.success(database.favouriteDao().getAllFavourite())
        }
    }

    override fun insert(favourite: Favourite) {
        GlobalScope.launch(Dispatchers.IO) {
            database.favouriteDao().insert(favourite)
        }
    }

    override fun addFavourite(favouriteResult: FavouriteResult) {
        val fav = Favourite(favourite!!["name"].toString(), favourite["lon"]!!.toString(), favourite["lat"]!!.toString())
        insert(fav)
        favouriteResult.success(null)
    }
}