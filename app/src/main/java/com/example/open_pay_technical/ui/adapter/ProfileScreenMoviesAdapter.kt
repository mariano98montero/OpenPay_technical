package com.example.open_pay_technical.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.open_pay_technical.R
import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.databinding.FragmentProfileMovieCardViewBinding
import com.example.open_pay_technical.util.Constants.SERVICE_IMAGE_URL

class ProfileScreenMoviesAdapter : RecyclerView.Adapter<ProfileScreenMoviesAdapter.ViewHolder>() {

    private val movies = mutableListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_profile_movie_card_view,
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
        private val binding = FragmentProfileMovieCardViewBinding.bind(itemView)
        fun bind(item: Movie) = with(itemView) {
            item.let {
                binding.apply {
                    if (item.poster.isNullOrEmpty())
                        profileScreenMovieCardImageView.setImageDrawable(
                            AppCompatResources.getDrawable(context, R.drawable.poster_placeholder)
                        )
                    profileScreenMovieCardTitle.text = it.title ?: it.name
                    it.poster?.let {
                        Glide.with(itemView.context)
                            .load("$SERVICE_IMAGE_URL$it")
                            .placeholder(R.drawable.poster_placeholder)
                            .centerCrop()
                            .into(profileScreenMovieCardImageView)
                    }
                    movieCardVoteTextView.text = resources.getString(
                        R.string.movies_card_vote_avg_text, it
                            .voteAverage.toString()
                    )
                    profileScreenMovieCardOverviewText.text = it.overview
                }
            }
        }
    }
}
