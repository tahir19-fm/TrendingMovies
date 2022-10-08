package com.example.trendingrepos

import android.util.Log
import com.example.trendingrepos.data.Results
import com.example.trendingrepos.data.movies
import com.example.trendingrepos.utils.ApiInterface
import com.example.trendingrepos.utils.RetrofitHelper
import kotlinx.coroutines.*
import java.util.ArrayList


class MainActivityRepo {
    @OptIn(DelicateCoroutinesApi::class)
    fun fetchRepositories(
        param: ApiCallbackWithRes<MutableList<Results>>
        , page:Int) {
        val list: MutableList<Results> = ArrayList()
    val retrofit=RetrofitHelper.getRetroInstance().create(ApiInterface::class.java)
        try {
            var movieData:movies
            val IMAGE_BASE="https://image.tmdb.org/t/p/w500"

             val job=   GlobalScope.launch {
                    val result = retrofit.getMovies(page)
                    movieData = result.body()!!


                    for (item in movieData.results) {
                        Log.d("res", item.title.toString())
                        var title: String
                        if (item.title != null) {
                            val imgUrl=IMAGE_BASE+item.posterPath.toString()
                            Log.d("image",imgUrl)
                            title = item.title.toString()
                            list.add(Results(item.adult,item.backdropPath,item.id,item.title,item.originalLanguage,item.originalTitle,item.overview,imgUrl,item.mediaType,item.genreIds,item.popularity,item.releaseDate,item.video,item.voteAverage,item.voteCount))
                        }

                    }
                }
            runBlocking {
                job.join() // wait until child coroutine completes
            }

            param.onSuccess(list)
        }catch (e:Exception){
            param.onError(e.stackTraceToString())
        }


    }
}
data class movieData (
val name:String,
val img:String,
val popularity:Double,
val voteAvg:Double
)
interface ApiCallbackWithRes<T> {
    fun onSuccess(response: T)
    fun onError(msg: String)
}