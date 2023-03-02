package com.example.open_pay_technical.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
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
            ExceptionDialogFragment.newInstance(getString(R.string.no_internet_error_message_text)).show(
                parentFragmentManager,
                ExceptionDialogFragment.EXCEPTION_DIALOG_FRAGMENT
            )
        }
        return binding.root
    }

    private fun updateUI(data: MoviesViewModel.MoviesScreenData) {
        when(data.state) {
            MoviesViewModel.MoviesScreenState.TOP_RATED_MOVIES_DATA_SUCCESS -> populateTopRatedMovies(data.movies)
            MoviesViewModel.MoviesScreenState.MOVIES_SAVED_SUCCESS -> TODO()
            MoviesViewModel.MoviesScreenState.MOVIES_SAVING_ERROR -> TODO()
            MoviesViewModel.MoviesScreenState.SHOW_LOADER -> binding.loaderAnimation.visibility = View.VISIBLE
            MoviesViewModel.MoviesScreenState.SERVICE_ERROR -> showExceptionDialogFragment(data.exception)
            MoviesViewModel.MoviesScreenState.POPULAR_MOVIES_DATA_SUCCESS -> populatePopularMovies(data.movies)
        }
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
    }

    private fun populateTopRatedMovies(movies: List<Movie>) {
        binding.movieFragmentTopRatedRecyclerView.adapter = topRatedMoviesAdapter
        binding.movieFragmentTopRatedRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        topRatedMoviesAdapter.updateList(movies)
        moviesViewModel.getPopularMovies()
        // Call the popular movies
    }

}
