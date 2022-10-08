package com.example.trendingrepos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.trendingrepos.data.Results
import com.example.trendingrepos.databinding.ActivityMovieDetailsBinding
import com.example.trendingrepos.utils.EspressoIdlingResource
import com.squareup.picasso.Picasso

class MovieDetailsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMovieDetailsBinding.inflate(layoutInflater) }
    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val posStr = intent.getStringExtra("position")
        Log.d("position", posStr.toString())
        val pos= posStr?.toInt()
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        if (BuildConfig.IS_TESTING.get())
            EspressoIdlingResource.increment()

    viewModel.getMovies().observe(this) {
    val data=it as List<Results>
        if (pos != null) {
        val dec=data.get(pos)
            val imgUrl="https://image.tmdb.org/t/p/w500${dec.posterPath}"
            binding.headingOfMovie.text=dec.originalTitle
            binding.rating.text="Rating ${dec.voteAverage}"
            binding.popularity.text="Popularity ${dec.popularity}"
            binding.tvDescription.text=dec.overview
            Picasso.get().load(imgUrl).into(binding.moviePoster)
        }
    }

    }
}