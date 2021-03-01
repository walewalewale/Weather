package com.decagon.weather.viewmodel

import androidx.annotation.Nullable

class ResultResponse<T>(private var status: Boolean, @Nullable private var data: T?) {

    fun getStatus():Boolean{
        return this.status
    }

    fun getData() : T{
        return this.data!!
    }
}