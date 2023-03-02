package com.example.open_pay_technical.ui.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.open_pay_technical.data.database.Database
import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.data.service.PopularMoviesService
import com.example.open_pay_technical.data.service.RecommendedMoviesService
import com.example.open_pay_technical.data.service.TopRatedMoviesService
import com.example.open_pay_technical.util.Constants
import com.example.open_pay_technical.util.Constants.POPULAR
import com.example.open_pay_technical.util.Constants.RECOMMENDATION
import com.example.open_pay_technical.util.Constants.TOP_RATED
import com.example.open_pay_technical.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val topRatedMoviesService: TopRatedMoviesService,
    private val popularMoviesService: PopularMoviesService,
    private val recommendedMoviesService: RecommendedMoviesService,
    private val database: Database
) : ViewModel() {

    private val liveData = MutableLiveData<MoviesScreenData>()

    fun getLiveData(): MutableLiveData<MoviesScreenData> = liveData

    fun fetchInitData() = viewModelScope.launch {
        liveData.postValue(MoviesScreenData(state = MoviesScreenState.SHOW_LOADER))
        withContext(Dispatchers.IO) { topRatedMoviesService.getTopRatedMovies() }.let { result ->
            when (result) {
                is Result.Success -> liveData.postValue(
                    MoviesScreenData(
                        state = MoviesScreenState.TOP_RATED_MOVIES_DATA_SUCCESS,
                        movies = result.data
                    )
                )
                is Result.Failure -> liveData.postValue(
                    MoviesScreenData(
                        state = MoviesScreenState.SERVICE_ERROR,
                        exception = result.exception.message.toString()
                    )
                )
            }
        }
    }

    fun getPopularMovies() = viewModelScope.launch {
        liveData.postValue(MoviesScreenData(state = MoviesScreenState.SHOW_LOADER))
        withContext(Dispatchers.IO) { popularMoviesService.getPopularMovies() }.let { result ->
            when (result) {
                is Result.Success -> liveData.postValue(
                    MoviesScreenData(
                        state = MoviesScreenState.POPULAR_MOVIES_DATA_SUCCESS,
                        movies = result.data
                    )
                )
                is Result.Failure -> liveData.postValue(
                    MoviesScreenData(
                        state = MoviesScreenState.SERVICE_ERROR,
                        exception = result.exception.message.toString()
                    )
                )
            }
        }
    }

    fun saveMovies(movies: List<Movie>) = viewModelScope.launch {
        withContext(Dispatchers.IO) { database.saveMovies(movies) }.let { result ->
            when (result) {
                is Result.Success -> liveData.postValue(MoviesScreenData(state = MoviesScreenState.MOVIES_SAVED_SUCCESS))
                is Result.Failure -> liveData.postValue(
                    MoviesScreenData(
                        state = MoviesScreenState.MOVIES_SAVING_ERROR,
                        exception = result.exception.message.toString()
                    )
                )
            }
        }
    }

    fun getRecommendedMovies(movieId: String) = viewModelScope.launch {
        liveData.postValue(MoviesScreenData(state = MoviesScreenState.SHOW_LOADER))
        withContext(Dispatchers.IO) { recommendedMoviesService.getRecommendedMovies(movieId) }.let { result ->
            when (result) {
                is Result.Success -> liveData.postValue(
                    MoviesScreenData(
                        state = MoviesScreenState.RECOMMENDED_MOVIES_DATA_SUCCESS,
                        movies = result.data
                    )
                )
                is Result.Failure -> liveData.postValue(
                    MoviesScreenData(
                        state = MoviesScreenState.SERVICE_ERROR,
                        exception = result.exception.message.toString()
                    )
                )
            }
        }
    }

    fun loadTopRatedMoviesFromLocal() = viewModelScope.launch {
        withContext(Dispatchers.IO) { database.getMoviesBySection(TOP_RATED) }.let { result ->
            when (result) {
                is Result.Success -> liveData.postValue(
                    MoviesScreenData(
                        state = MoviesScreenState.TOP_RATED_MOVIES_DATA_SUCCESS,
                        movies = result.data
                    )
                )
                is Result.Failure -> liveData.postValue(
                    MoviesScreenData(
                        state = MoviesScreenState.SERVICE_ERROR,
                        exception = result.exception.message.toString()
                    )
                )
            }
        }
    }

    fun loadRecommendedMoviesFromLocal() = viewModelScope.launch {
        withContext(Dispatchers.IO) { database.getMoviesBySection(RECOMMENDATION) }.let { result ->
            when (result) {
                is Result.Success -> liveData.postValue(
                    MoviesScreenData(
                        state = MoviesScreenState.RECOMMENDED_MOVIES_DATA_SUCCESS,
                        movies = result.data
                    )
                )
                is Result.Failure -> liveData.postValue(
                    MoviesScreenData(
                        state = MoviesScreenState.SERVICE_ERROR,
                        exception = result.exception.message.toString()
                    )
                )
            }
        }
    }

    fun loadPopularMoviesFromLocal() = viewModelScope.launch {
        withContext(Dispatchers.IO) { database.getMoviesBySection(POPULAR) }.let { result ->
            when (result) {
                is Result.Success -> liveData.postValue(
                    MoviesScreenData(
                        state = MoviesScreenState.POPULAR_MOVIES_DATA_SUCCESS,
                        movies = result.data
                    )
                )
                is Result.Failure -> liveData.postValue(
                    MoviesScreenData(
                        state = MoviesScreenState.SERVICE_ERROR,
                        exception = result.exception.message.toString()
                    )
                )
            }
        }
    }

    data class MoviesScreenData(
        var state: MoviesScreenState,
        var movies: List<Movie> = emptyList(),
        var exception: String = Constants.EMPTY_STRING
    )

    enum class MoviesScreenState {
        TOP_RATED_MOVIES_DATA_SUCCESS,
        RECOMMENDED_MOVIES_DATA_SUCCESS,
        POPULAR_MOVIES_DATA_SUCCESS,
        MOVIES_SAVED_SUCCESS,
        MOVIES_SAVING_ERROR,
        SHOW_LOADER,
        SERVICE_ERROR
    }
}
