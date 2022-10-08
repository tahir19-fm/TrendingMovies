package com.example.trendingrepos.utils

import com.example.trendingrepos.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitHelper {
    private const val baseUrl = BuildConfig.BASE_URL

   fun getRetroInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }

}