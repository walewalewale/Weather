package com.decagon.weather

import org.junit.Test

import org.junit.Assert.*

class DateTimeTest {
    @Test
    fun testDate() {
        val time = 1614540661
        val res = DateTimeUtil.getDay(time.toLong())
        assertEquals("Sunday", res)
    }
}