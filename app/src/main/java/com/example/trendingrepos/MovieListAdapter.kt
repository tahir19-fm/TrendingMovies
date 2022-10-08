package com.example.trendingrepos

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.trendingrepos.data.Results
import com.example.trendingrepos.databinding.RepoRowItemBinding
import com.squareup.picasso.Picasso


class MovieListAdapter(
    private val repositories: List<Results>
) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListAdapter.ViewHolder {
        val binding: RepoRowItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.repo_row_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return repositories.size
    }

    override fun onBindViewHolder(holder: MovieListAdapter.ViewHolder, position: Int) {

        holder.itemView.setOnClickListener{v->
            val intent = Intent(v.context, MovieDetailsActivity::class.java)
            intent.putExtra("position", position.toString())
            Log.d("position", position.toString())
            v.context.startActivity(intent)
        }
        holder.setData(repositories[position])
    }
    inner class ViewHolder(private val binding: RepoRowItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun setData(repo: Results) {
          //  binding.tvRepoName.text = repo.name
           //  binding.ivMovie.text = BaseApplication.appContext.getString(R.string.repo_desc, repo.repoDesc)
            Picasso.get().load(repo.posterPath).into(binding.ivMovie)

        }
    }
}