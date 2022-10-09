package com.example.trendingrepos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.trendingrepos.databinding.ActivityMovieDetailsBinding
import com.example.trendingrepos.utils.CheckNetwork
import com.example.trendingrepos.utils.EspressoIdlingResource
import com.squareup.picasso.Picasso

class MovieDetailsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMovieDetailsBinding.inflate(layoutInflater) }
    private lateinit var viewModel: DetailActivityViewModel
    private lateinit var checkNetwork: CheckNetwork
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val posStr = intent.getStringExtra("id")
        val id= posStr?.toInt()
        viewModel = ViewModelProvider(this)[DetailActivityViewModel::class.java]
        if (BuildConfig.IS_TESTING.get())
            EspressoIdlingResource.increment()
        if (posStr != null) {
            if (checkNetwork.isConnected(this)){
                viewModel.fetchMovieDetail(posStr)
            }else{
                Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show()
            }

        }

        viewModel.getMovies().observe(this) {
            val dec=it as RequiuredData
                Log.d("id", dec.toString())
                binding.headingOfMovie.text=dec.title
                binding.rating.text="Rating ${dec.rating}"
                binding.popularity.text="Popularity ${dec.popularity}"
                binding.tvDescription.text=dec.details
                Picasso.get().load(dec.img).into(binding.moviePoster)
            }
        }

    }
