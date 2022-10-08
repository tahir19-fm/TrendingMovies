package com.example.trendingrepos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trendingrepos.data.Results
import com.example.trendingrepos.databinding.ActivityMainBinding
import com.example.trendingrepos.utils.EspressoIdlingResource

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private var page =1
    private var isLoading=false
    private var isLastPage=false
    private  var  reposList: MutableList<Results> = mutableListOf()
    private lateinit var list:MutableList<Results>
    companion object{
        const val SORT_BY_POPULARITY=1
        const val SORT_BY_RATING=2
        const val NO_SORTING=3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val layoutManager=GridLayoutManager(this,2)
        list=ArrayList()
        val adapter = MovieListAdapter(reposList)
        binding.rvRoot.layoutManager=layoutManager
        binding.rvRoot.adapter = adapter
        binding.rvRoot.setHasFixedSize(true)

        binding.rvRoot.addOnScrollListener(object : RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
            {
                super.onScrolled(recyclerView, dx, dy)
               val visibleItem=layoutManager.childCount
                val totalItem=layoutManager.itemCount
                val firstVisibleItem=layoutManager.findFirstVisibleItemPosition()
                Log.d("scroll", dy.toString())
                if(!isLastPage&&!isLoading&&dy>0) {
                   if((visibleItem+firstVisibleItem>=totalItem)&&firstVisibleItem>=0){
                       page++
                       isLoading=true
                       binding.progressBar.visibility= View.VISIBLE
                       loadMore()
                   }
                }

            }
        })

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        if (BuildConfig.IS_TESTING.get())
            EspressoIdlingResource.increment()

        viewModel.getMovies().observe(this, Observer {
//            if(reposList.isNotEmpty()) {
//                reposList.clear()
//            }
            binding.sortByPopularity.setTextColor(resources.getColor(R.color.black))
            binding.sortByVoting.setTextColor(resources.getColor(R.color.black))

            reposList.addAll(it)
            if (viewModel.sortState.value== SORT_BY_POPULARITY){
                binding.sortByPopularity.setTextColor(resources.getColor(R.color.red))
                binding.sortByVoting.setTextColor(resources.getColor(R.color.black))
                reposList.sortWith(compareByDescending { it.popularity })
            }else if (viewModel.sortState.value== SORT_BY_RATING){
                binding.sortByVoting.setTextColor(resources.getColor(R.color.red))
                binding.sortByPopularity.setTextColor(resources.getColor(R.color.black))
                reposList.sortWith(compareByDescending { it.voteAverage })
            }
            adapter.notifyDataSetChanged()
            viewModel.setDataShare(reposList)


             //  adapter.notifyItemRangeInserted(layoutManager.itemCount-1, reposList.size)

            binding.progressBar.visibility=View.GONE
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
            viewModel.setSortState(NO_SORTING)
            isLoading=true
            reposList.clear()
            viewModel.fetchMovies(1)
            page=1
        }
        binding.sortByPopularity.setOnClickListener{
            viewModel.setSortState(SORT_BY_POPULARITY)
            reposList.clear()
            viewModel.fetchMovies(1)
        }
        binding.sortByVoting.setOnClickListener{
            viewModel.setSortState(SORT_BY_RATING)
            reposList.clear()
            viewModel.fetchMovies(1)
        }
    }

    private fun loadMore() {
        viewModel.fetchMovies(page)
    }
}