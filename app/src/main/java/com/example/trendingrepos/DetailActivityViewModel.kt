package com.example.trendingrepos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailActivityViewModel: ViewModel()  {
    private val movieDataList : MutableLiveData<RequiuredData> = MutableLiveData()
    private val errorResponse = MutableLiveData<String>()
    private val isRefreshing = MutableLiveData<Boolean>()
    private val repo: MovieDetailRepo = MovieDetailRepo()

    fun getMovies() : LiveData<RequiuredData> {
        return movieDataList
    }
    fun getErrorReponse() : LiveData<String> {
        return errorResponse
    }
    fun getisRefreshing() : LiveData<Boolean> {
        return isRefreshing
    }
    fun fetchMovieDetail(id: String) {
        isRefreshing.value = true
        repo.fetchRepositories(object : ApiCallbackWithRes<RequiuredData> {
            override fun onSuccess(response: RequiuredData) {
                movieDataList.postValue(response)

            }

            override fun onError(msg: String) {
                errorResponse.postValue(msg)

            }

        },id)
    }
}