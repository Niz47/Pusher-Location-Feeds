package com.example.zmh.pusherlocationfeeds

import okhttp3.OkHttpClient
import retrofit2.Retrofit

class Client {
    fun getClient(): Service {
        val httpClient = OkHttpClient.Builder()

        val builder = Retrofit.Builder()
                .baseUrl("your_server_url")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder
                .client(httpClient.build())
                .build()

        return retrofit.create(Service::class.java)
    }
}