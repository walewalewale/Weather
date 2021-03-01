package com.decagon.weather.repo

interface IFavouriteRepo {
    fun addFavourite(favouriteResult: FavouriteResult)
    fun getCurrentOffline(favouriteResult: FavouriteResult)
}