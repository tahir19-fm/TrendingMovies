package com.example.trendingrepos

import android.util.Log
import com.example.trendingrepos.data.movieDetails
import com.example.trendingrepos.utils.ApiInterface
import com.example.trendingrepos.utils.RetrofitHelper
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MovieDetailRepo{
@OptIn(DelicateCoroutinesApi::class)
fun fetchRepositories(
    param: ApiCallbackWithRes<RequiuredData>
    , id: String
) {
    val list: MutableList<RequiuredData> = ArrayList()
    val retrofit= RetrofitHelper.getRetroInstance().create(ApiInterface::class.java)
    try {
        var movieData: movieDetails
        val IMAGE_BASE="https://image.tmdb.org/t/p/w500"

        val job=   GlobalScope.launch {
            val result =  retrofit.getMovieDetail(id)
          // movieData = result.body()!!
          val res=result.body()
            val id=res?.get("id")?.toString()
           Log.d("moviedet",id.toString())

        }
        runBlocking {
            job.join() // wait until child coroutine completes
        }
    }catch (e:Exception){
        param.onError(e.stackTraceToString())
    }
}
}
data class RequiuredData(
    val img:String,
    val details: String,
    val rating :String,
    val popularity:String,
    val title:String
)
