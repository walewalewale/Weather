package com.decagon.weather.connection.rectrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ApiClient {

    companion object{
        fun getClient() : Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val ongoing = chain.request().newBuilder()
//                    ongoing.addHeader("APPID", API_TOKEN)
                    chain.proceed(ongoing.build())
                })
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
//                .sslSocketFactory(trustAllSslSocketFactory, trustAllCerts[0] as X509TrustManager) //comment out when going live
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        }

        /*
        * Note: this is very bad practice and should NOT be used in production, Certificates are to be provided.
        */
        private val trustAllCerts= arrayOf<TrustManager>( object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
                //Todo ...
            }

            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
                //Todo ...
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        } )

        var trustAllSslContext : SSLContext?=null

        init {
            trustAllSslContext = SSLContext.getInstance("SSL")
            trustAllSslContext!!.init(
                null,
                trustAllCerts,
                SecureRandom()
            )
        }

        private val trustAllSslSocketFactory: SSLSocketFactory =
            trustAllSslContext!!.socketFactory
    }
}