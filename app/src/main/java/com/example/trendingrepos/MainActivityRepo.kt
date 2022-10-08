package com.example.trendingrepos

import android.util.Log
import com.example.trendingrepos.data.movies
import com.example.trendingrepos.utils.ApiInterface
import com.example.trendingrepos.utils.RetrofitHelper
import kotlinx.coroutines.*
import java.util.ArrayList


class MainActivityRepo {
    @OptIn(DelicateCoroutinesApi::class)
    fun fetchRepositories(
        param: ApiCallbackWithRes<MutableList<movieData>>
    ,page:Int) {
        val list: MutableList<movieData> = ArrayList()
    val retrofit=RetrofitHelper.getRetroInstance().create(ApiInterface::class.java)
        try {
            var movieData:movies


             val job=   GlobalScope.launch {
                    val result = retrofit.getMovies(page)
                    movieData = result.body()!!


                    for (item in movieData.results) {
                        Log.d("res", item.title.toString())
                        var title: String
                        if (item.title == null) {
                            title = "sorry not able to load"
                        } else {
                            title = item.title.toString()
                        }
                        val s="https://m.media-amazon.com/images/I/71niXI3lxlL._SY679_.jpg"
                        list.add(movieData(title, s))
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
val img:String
)
interface ApiCallbackWithRes<T> {
    fun onSuccess(response: T)
    fun onError(msg: String)
}