package com.example.trendingrepos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trendingrepos.databinding.ActivityMainBinding
import com.example.trendingrepos.utils.EspressoIdlingResource

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private var page =1
    private var isLoading=false
    private var isLastPage=false
    val reposList: MutableList<movieData> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val layoutManager=GridLayoutManager(this,2)

        val adapter = MovieListAdapter(reposList)
        binding.rvRoot.layoutManager=layoutManager
        binding.rvRoot.adapter = adapter
        binding.rvRoot.setHasFixedSize(true)
        binding.rvRoot.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        binding.rvRoot.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
            {
                super.onScrolled(recyclerView, dx, dy)
               val visibleItem=layoutManager.childCount
                val totalItem=layoutManager.itemCount
                val firstVisibleItem=layoutManager.findFirstVisibleItemPosition()
                if(!isLastPage&&!isLoading) {
                   if((visibleItem+firstVisibleItem>=totalItem)&&firstVisibleItem>=0){
                       page++
                       loadMore()
                   }
                }

            }
        })

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        if (BuildConfig.IS_TESTING.get())
            EspressoIdlingResource.increment()

        viewModel.getRepositories().observe(this, Observer {
//            if(reposList.isNotEmpty()) {
//                reposList.clear()
//            }
            reposList.addAll(it)

                adapter.notifyDataSetChanged()
            isLoading=false

            if (!EspressoIdlingResource.idlingResource.isIdleNow) {
                EspressoIdlingResource.decrement(); // Set app as idle.
            }
        })

        viewModel.getErrorReponse().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
        viewModel.getisRefreshing().observe(this, Observer {
            binding.swipeRefresh.isRefreshing = it
        })
        binding.swipeRefresh.setOnRefreshListener {
            isLoading=true
            reposList.clear()
            viewModel.fetchRepositories(1)
            page=1
        }
    }

    private fun loadMore() {
        isLoading=true
        viewModel.fetchRepositories(page)
    }
}