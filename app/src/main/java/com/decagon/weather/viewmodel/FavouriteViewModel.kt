package com.decagon.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.decagon.weather.data.room.entity.Favourite
import com.decagon.weather.repo.FavouriteRepository
import com.decagon.weather.repo.FavouriteResult
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException

class FavouriteViewModel(application: Application, private val repository: FavouriteRepository) : AndroidViewModel(application) {
    private val lock = Mutex()

    private val _favourite = MutableLiveData<ResultResponse<List<Favourite>>>()

    val favourite: LiveData<ResultResponse<List<Favourite>>>
    get() = _favourite

    private val _addFavourite = MutableLiveData<Boolean>()

    val addFavourite: LiveData<Boolean>
        get() = _addFavourite

    fun fetchDataOffline() = viewModelScope.launch {
        lock.withLock {
            try {
                repository.getCurrentOffline(object : FavouriteResult{
                    override fun success(value: List<Favourite>?) {
                        _favourite.postValue(ResultResponse(status = true, data = value))
                    }

                    override fun error(value: String) {
                        _favourite.postValue(ResultResponse(status = false, null))
                    }
                })
            } catch (networkError: IOException) {
                _favourite.postValue(ResultResponse(status = false, null))
            }
        }
    }

    fun addToFavourite() = viewModelScope.launch {
        lock.withLock {
            try {
                repository.addFavourite(object : FavouriteResult{
                    override fun success(value: List<Favourite>?) {
                        _addFavourite.postValue(true)
                    }

                    override fun error(value: String) {
                        _addFavourite.postValue(false)
                    }
                })
            } catch (networkError: IOException) {
                _addFavourite.postValue(false)
            }
        }
    }

    class Factory(val app: Application,private val repository: FavouriteRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavouriteViewModel(app, repository) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}
