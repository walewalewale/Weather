package com.decagon.weather.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.decagon.weather.data.room.entity.Forecast
import com.decagon.weather.repo.ForecastRepository
import com.decagon.weather.repo.ForecastResult
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException

class ForecastViewModel(application: Application, private val repository: ForecastRepository) : AndroidViewModel(application) {
    private val lock = Mutex()

    private var _connectionState = MutableLiveData(ConnectionStatus.UPDATING)
    val connectionState: LiveData<ConnectionStatus>
        get() = _connectionState

    private val _forecast = MutableLiveData<ResultResponse<List<Forecast>>>()

    val forecast: LiveData<ResultResponse<List<Forecast>>>
    get() = _forecast

    init {
        fetchDataOffline()
        fetchDataFromNetwork()
    }

    private fun fetchDataFromNetwork() = viewModelScope.launch {
        lock.withLock {
            try {
                repository.getCurrentOnline(object : ForecastResult {
                    override fun success(value: List<Forecast>) {
                        _forecast.postValue(ResultResponse(status = true, data = value))
                        _connectionState.postValue(ConnectionStatus.SUCCESS)
                    }

                    override fun error(value: String) {
                        _forecast.postValue(ResultResponse(status = false, null))
                        _connectionState.postValue(ConnectionStatus.FAILED)
                    }
                })
            } catch (networkError: IOException) {
                _forecast.postValue(ResultResponse(status = false, null))
                _connectionState.postValue(ConnectionStatus.OFFLINE)
            }
        }
    }

    private fun fetchDataOffline() = viewModelScope.launch {
        lock.withLock {
            try {
                repository.getCurrentOffline(object : ForecastResult{
                    override fun success(value: List<Forecast>) {
                        _forecast.postValue(ResultResponse(status = true, data = value))
                    }

                    override fun error(value: String) {
                        _forecast.postValue(ResultResponse(status = false, null))
                    }
                })
            } catch (networkError: IOException) {
                _forecast.postValue(ResultResponse(status = false, null))
            }
        }
    }

    class Factory(val app: Application,private val repository: ForecastRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ForecastViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ForecastViewModel(app, repository) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}
