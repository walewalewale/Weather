package com.decagon.weather

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.decagon.weather.connection.rectrofit.ApiCalls
import com.decagon.weather.model.WeatherDTO
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import retrofit2.*


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class ApiCallHealthCheckTest {
    lateinit var sampleParam : Map<String, String>

    @Before
    fun initValidString() {
        sampleParam = mapOf(
            "lon" to "3.4823",
            "lat" to "6.4502",
            "units" to "metric",
            "appid" to "6998fae3505521d819e3afa3c4e8330d"
        )
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun apiCallHealCheckTestOkWithNoNNullBody() {
        val response = ApiCalls(sampleParam).getCurrentApi().execute()
        Assert.assertEquals("200", response.code().toString())
        Assert.assertNotEquals(null, response.body())
    }
}