package com.decagon.weather

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testDate() {
        val time = 1560507488
        val res = DateTimeUtil.getDateString(time.toLong())
        assertEquals("14 June 2019, 11:18:08", res)
    }
}