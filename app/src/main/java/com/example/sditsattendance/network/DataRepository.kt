package com.example.sditsattendance.network

import com.example.sditsattendance.api.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataRepository {
    fun create():ResponeService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://192.168.1.201/")
            .build()

        return retrofit.create(ResponeService::class.java)
    }
}