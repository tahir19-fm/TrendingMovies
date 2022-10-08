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
        fetchMovies(1)
    }

    fun getMovies() : LiveData<List<movieData>> {
        return movieDataList
    }
    fun getErrorReponse() : LiveData<String> {
        return errorResponse
    }
    fun getisRefreshing() : LiveData<Boolean> {
        return isRefreshing
    }
    fun fetchMovies(page:Int) {
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
    private val _sortState = MutableLiveData<Int>()
    val sortState : LiveData<Int>
        get() = _sortState

    fun setSortState(state:Int){
        _sortState.value=state
    }

}