package com.decagon.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.decagon.weather.data.room.entity.Weather
import com.decagon.weather.repo.WeatherResult
import com.decagon.weather.repo.WeatherRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException

class WeatherViewModel(application: Application,private val repository: WeatherRepository) : AndroidViewModel(application) {
    private val lock = Mutex()

    private var _connectionState = MutableLiveData(ConnectionStatus.UPDATING)
    val connectionState: LiveData<ConnectionStatus>
        get() = _connectionState

    private val _weather = MutableLiveData<ResultResponse<Weather>>()

    val weather: LiveData<ResultResponse<Weather>>
    get() = _weather

    init {
        fetchDataOffline()
        fetchDataFromNetwork()
    }

    private fun fetchDataFromNetwork() = viewModelScope.launch {
        lock.withLock {
            try {
                repository.getCurrentOnline(object : WeatherResult{
                    override fun success(value: Weather) {
                        _weather.postValue(ResultResponse(status = true, data = value))
                        _connectionState.postValue(ConnectionStatus.SUCCESS)
                    }

                    override fun error(value: String) {
                        _weather.postValue(ResultResponse(status = false, null))
                        _connectionState.postValue(ConnectionStatus.FAILED)
                    }
                })
            } catch (networkError: IOException) {
                _weather.postValue(ResultResponse(status = false, null))
                _connectionState.postValue(ConnectionStatus.OFFLINE)
            }
        }
    }

    private fun fetchDataOffline() = viewModelScope.launch {
        lock.withLock {
            try {
                repository.getCurrentOffline(object : WeatherResult{
                    override fun success(value: Weather) {
                        _weather.postValue(ResultResponse(status = true, data = value))
                    }

                    override fun error(value: String) {
                        _weather.postValue(ResultResponse(status = false, null))
                    }
                })
            } catch (networkError: IOException) {
                _weather.postValue(ResultResponse(status = false, null))
            }
        }
    }

    class Factory(val app: Application,private val repository: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WeatherViewModel(app, repository) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}
