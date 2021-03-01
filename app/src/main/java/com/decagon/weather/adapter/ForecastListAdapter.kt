package com.decagon.weather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.decagon.weather.R
import com.decagon.weather.data.room.entity.Forecast

class ForecastListAdapter(private val forecastList: ArrayList<Forecast>, private val actions : ForecastActions) : RecyclerView.Adapter<ForecastListAdapter.UsersListAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListAdapterViewHolder {
        return UsersListAdapterViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: UsersListAdapterViewHolder, position: Int) {
        holder.bind(forecastList[position], actions)
    }

    override fun getItemCount(): Int {
        return forecastList.size;
    }

    class UsersListAdapterViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_day, parent, false))
    {
        private var itemParentLayout: ConstraintLayout? = null
        private var date: TextView? = null
        private var weatherIcon: ImageView? = null
        private var degree: TextView? = null

        init {
            itemParentLayout = itemView.findViewById(R.id.item_parent_layout)
            date = itemView.findViewById(R.id.date)
            weatherIcon = itemView.findViewById(R.id.weather_icon)
            degree = itemView.findViewById(R.id.degree)
        }

        fun bind(forecast: Forecast, actions: ForecastActions) {
            actions.renderItem(forecast, itemParentLayout, date, weatherIcon, degree)
        }
    }
}