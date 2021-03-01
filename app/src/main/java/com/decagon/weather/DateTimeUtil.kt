package com.decagon.weather

import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {
   companion object{
       private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.ENGLISH)
       fun getDateString(time: Long) : String = simpleDateFormat.format(time * 1000L)

       fun getDay(time:Long) : String{
           val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.ENGLISH)
           simpleDateFormat.format(time * 1000L)

           val sdf = SimpleDateFormat("EEEE", Locale.ENGLISH)
          return sdf.format(simpleDateFormat.calendar.time)
       }
   }
}