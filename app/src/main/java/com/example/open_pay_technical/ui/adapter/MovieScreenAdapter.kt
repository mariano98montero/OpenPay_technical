package com.example.open_pay_technical.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.open_pay_technical.R
import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.databinding.FragmentMoviesMovieCardBinding
import com.example.open_pay_technical.util.Constants

class MovieScreenAdapter : RecyclerView.Adapter<MovieScreenAdapter.ViewHolder>() {

    private val movies = mutableListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_movies_movie_card,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun updateList(moviesList: List<Movie>) {
        movies.addAll(moviesList)
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = FragmentMoviesMovieCardBinding.bind(itemView)
        fun bind(item: Movie) = with(itemView) {
            item.let {
                binding.apply {
                    if (item.poster.isNullOrEmpty())
                        fragmentMoviesMoviePosterImageView.setImageDrawable(
                            AppCompatResources.getDrawable(context, R.drawable.poster_placeholder)
                        )
                    fragmentMoviesMovieTitleTextView.text = it.title ?: it.name
                    it.poster?.let {
                        Glide.with(itemView.context)
                            .load("${Constants.SERVICE_IMAGE_URL}$it")
                            .placeholder(R.drawable.poster_placeholder)
                            .centerCrop()
                            .into(fragmentMoviesMoviePosterImageView)
                    }
                    fragmentMoviesMovieValueTextView.text = it.voteAverage.toString()
                }
            }
        }
    }
}
