package com.decagon.weather

import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {
   companion object{
       private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.ROOT)
       fun getDateString(time: Long) : String = simpleDateFormat.format(time * 1000L)

       fun getDay(time:Long) : String{
           val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.ROOT)
           simpleDateFormat.format(time * 1000L)

           val sdf = SimpleDateFormat("EEEE", Locale.ROOT)
          return sdf.format(simpleDateFormat.calendar.time)
       }
   }
}