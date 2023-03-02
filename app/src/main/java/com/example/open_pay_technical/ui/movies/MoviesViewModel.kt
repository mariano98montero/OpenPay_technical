package com.example.open_pay_technical.ui.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.data.service.PopularMoviesService
import com.example.open_pay_technical.data.service.TopRatedMoviesService
import com.example.open_pay_technical.ui.profile.ProfileViewModel
import com.example.open_pay_technical.util.Constants
import com.example.open_pay_technical.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val topRatedMoviesService: TopRatedMoviesService,
    private val popularMoviesService: PopularMoviesService
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

    fun getPopularMovies() = viewModelScope.launch{
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

    data class MoviesScreenData(
        var state: MoviesScreenState,
        var movies: List<Movie> = emptyList(),
        var exception: String = Constants.EMPTY_STRING
    )

    enum class MoviesScreenState {
        TOP_RATED_MOVIES_DATA_SUCCESS,
        POPULAR_MOVIES_DATA_SUCCESS,
        MOVIES_SAVED_SUCCESS,
        MOVIES_SAVING_ERROR,
        SHOW_LOADER,
        SERVICE_ERROR
    }
}
