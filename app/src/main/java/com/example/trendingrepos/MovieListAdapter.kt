package com.example.trendingrepos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trendingrepos.databinding.RepoRowItemBinding
import com.squareup.picasso.Picasso


class MovieListAdapter(
    private val repositories: List<movieData>
) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListAdapter.ViewHolder {
        val binding: RepoRowItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.repo_row_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return repositories.size
    }

    override fun onBindViewHolder(holder: MovieListAdapter.ViewHolder, position: Int) {
        holder.setData(repositories[position])
    }
    inner class ViewHolder(private val binding: RepoRowItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun setData(repo: movieData) {
          //  binding.tvRepoName.text = repo.name
           //  binding.ivMovie.text = BaseApplication.appContext.getString(R.string.repo_desc, repo.repoDesc)
            Picasso.get().load(repo.img).into(binding.ivMovie)

        }
    }
}