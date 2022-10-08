package com.example.trendingrepos.utils

import com.example.trendingrepos.data.Results
import com.example.trendingrepos.data.movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("/3/trending/all/week?api_key=02781ffe196aa2b6957d413a384b5c82")
    suspend fun getMovies(@Query("page") page:Int) : Response<movies>
}