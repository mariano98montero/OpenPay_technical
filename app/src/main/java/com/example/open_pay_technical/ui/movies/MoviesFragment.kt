package com.example.open_pay_technical.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.open_pay_technical.R
import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.databinding.FragmentMoviesBinding
import com.example.open_pay_technical.ui.adapter.MovieScreenAdapter
import com.example.open_pay_technical.util.ExceptionDialogFragment
import com.example.open_pay_technical.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding

    private val topRatedMoviesAdapter = MovieScreenAdapter()
    private val popularMoviesAdapter = MovieScreenAdapter()
    private val recommendedMoviesAdapter = MovieScreenAdapter()
    private val moviesViewModel: MoviesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater)
        moviesViewModel.getLiveData().observe({ lifecycle }, ::updateUI)
        if (context?.let { Util.isDeviceOnline(it) } == true) {
            moviesViewModel.fetchInitData()
        } else {
            showExceptionDialogFragment(getString(R.string.no_internet_error_message_text))
            moviesViewModel.loadTopRatedMoviesFromLocal()
        }
        return binding.root
    }

    private fun updateUI(data: MoviesViewModel.MoviesScreenData) {
        when(data.state) {
            MoviesViewModel.MoviesScreenState.TOP_RATED_MOVIES_DATA_SUCCESS -> populateTopRatedMovies(data.movies)
            MoviesViewModel.MoviesScreenState.MOVIES_SAVED_SUCCESS -> Toast.makeText(
                context, getString(R.string.database_saved_successfully_message_text), Toast
                    .LENGTH_SHORT
            ).show()
            MoviesViewModel.MoviesScreenState.MOVIES_SAVING_ERROR -> showExceptionDialogFragment(data.exception)
            MoviesViewModel.MoviesScreenState.SHOW_LOADER -> binding.loaderAnimation.visibility = View.VISIBLE
            MoviesViewModel.MoviesScreenState.SERVICE_ERROR -> showExceptionDialogFragment(data.exception)
            MoviesViewModel.MoviesScreenState.POPULAR_MOVIES_DATA_SUCCESS -> populatePopularMovies(data.movies)
            MoviesViewModel.MoviesScreenState.RECOMMENDED_MOVIES_DATA_SUCCESS -> populateRecommendedMovies(data.movies)
        }
    }

    private fun populateRecommendedMovies(movies: List<Movie>) {
        if (context?.let { Util.isDeviceOnline(it) } == true) {
            moviesViewModel.saveMovies(movies)
            moviesViewModel.getPopularMovies()
        } else {
            moviesViewModel.loadPopularMoviesFromLocal()
        }
        binding.loaderAnimation.visibility = View.GONE
        binding.movieFragmentRecommendedRecyclerView.adapter = recommendedMoviesAdapter
        binding.movieFragmentRecommendedRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recommendedMoviesAdapter.updateList(movies)
    }

    private fun showExceptionDialogFragment(title: String) {
        ExceptionDialogFragment.newInstance(title)
            .show(
                parentFragmentManager,
                ExceptionDialogFragment.EXCEPTION_DIALOG_FRAGMENT
            )
    }

    private fun populatePopularMovies(movies: List<Movie>) {
        binding.loaderAnimation.visibility = View.GONE
        binding.movieFragmentPopularRecyclerView.adapter = popularMoviesAdapter
        binding.movieFragmentPopularRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        popularMoviesAdapter.updateList(movies)
        moviesViewModel.saveMovies(movies)
    }

    private fun populateTopRatedMovies(movies: List<Movie>) {
        if (context?.let { Util.isDeviceOnline(it) } == true) {
            moviesViewModel.saveMovies(movies)
            moviesViewModel.getRecommendedMovies(movies.first().id)
        } else {
            moviesViewModel.loadRecommendedMoviesFromLocal()
        }
        binding.movieFragmentTopRatedRecyclerView.adapter = topRatedMoviesAdapter
        binding.movieFragmentTopRatedRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.fragmentMoviesRecommendedTitleTextView.text = resources.getString(
            R.string.fragment_movies_recommended_title_text, movies.first().name?: movies.first().title)
        topRatedMoviesAdapter.updateList(movies)

    }

}
