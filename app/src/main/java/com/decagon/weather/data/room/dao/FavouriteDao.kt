package com.decagon.weather.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.decagon.weather.data.room.entity.Favourite

@Dao
interface FavouriteDao {

    @Insert(onConflict = REPLACE)
    fun insert(favourite: Favourite?)

    @Query("DELETE FROM favourite_table")
    fun deleteAll()

    @Query("SELECT * from favourite_table ORDER BY name ASC")
    fun getAllFavourite(): List<Favourite>
}