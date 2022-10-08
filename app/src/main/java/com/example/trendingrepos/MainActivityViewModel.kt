package com.example.trendingrepos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    private val movieDataList : MutableLiveData<List<movieData>> = MutableLiveData()
    private val errorResponse = MutableLiveData<String>()
    private val isRefreshing = MutableLiveData<Boolean>()
    private val repo: MainActivityRepo = MainActivityRepo()

    init {
        isRefreshing.value = false
        fetchRepositories(1)
    }

    fun getRepositories() : LiveData<List<movieData>> {
        return movieDataList
    }
    fun getErrorReponse() : LiveData<String> {
        return errorResponse
    }
    fun getisRefreshing() : LiveData<Boolean> {
        return isRefreshing
    }
    fun fetchRepositories(page:Int) {
        isRefreshing.value = true
         repo.fetchRepositories(object : ApiCallbackWithRes<MutableList<movieData>> {
             override fun onSuccess(response: MutableList<movieData>) {
                 movieDataList.postValue(response)
                 isRefreshing.value = false

             }

             override fun onError(msg: String) {
                 errorResponse.postValue(msg)
                 isRefreshing.value = false

             }

         },page)
    }
}