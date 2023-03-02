package com.example.open_pay_technical.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.open_pay_technical.data.database.Database
import com.example.open_pay_technical.data.entity.Actor
import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.data.service.CombinedCreditsService
import com.example.open_pay_technical.data.service.MostPopularActorService
import com.example.open_pay_technical.util.Constants.EMPTY_STRING
import com.example.open_pay_technical.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val mostPopularActor: MostPopularActorService,
    private val combinedCreditsService: CombinedCreditsService,
    private val database: Database
) : ViewModel() {

    private val liveData = MutableLiveData<ProfileScreenData>()

    fun getLiveData(): MutableLiveData<ProfileScreenData> = liveData

    fun fetchInitData() = viewModelScope.launch {
        liveData.postValue(ProfileScreenData(state = ProfileScreenState.SHOW_LOADER))
        withContext(Dispatchers.IO) { mostPopularActor.getMostPopularActors() }.let { result ->
            when (result) {
                is Result.Success -> liveData.postValue(
                    ProfileScreenData(state = ProfileScreenState.ACTOR_DATA_SUCCESS, actor = result.data)
                )
                is Result.Failure -> liveData.postValue(
                    ProfileScreenData(
                        state = ProfileScreenState.SERVICE_ERROR,
                        exception = result.exception.message.toString()
                    )
                )
            }
        }
    }

    fun getCombinedCredits(actorId: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) { combinedCreditsService.getCombinedCredits(actorId) }
            .let { result ->
                when (result) {
                    is Result.Success -> liveData.postValue(
                        ProfileScreenData(state = ProfileScreenState.SHOW_CREDIT_MOVIES, movies = result.data)
                    )
                    is Result.Failure -> liveData.postValue(
                        ProfileScreenData(
                            state = ProfileScreenState.SERVICE_ERROR,
                            exception = result.exception.message.toString()
                        )
                    )
                }
            }
    }

    fun saveActorOnLocal(actor: Actor) = viewModelScope.launch {
        withContext(Dispatchers.IO) { database.saveActor(actor) }.let { result ->
            when (result) {
                is Result.Success -> liveData.postValue(ProfileScreenData(state = ProfileScreenState.ACTOR_SAVED_SUCCESS))
                is Result.Failure -> liveData.postValue((ProfileScreenData(state = ProfileScreenState.ACTOR_SAVING_ERROR)))
            }
        }
    }

    data class ProfileScreenData(
        var state: ProfileScreenState,
        var actor: Actor? = null,
        var movies: List<Movie> = emptyList(),
        var exception: String = EMPTY_STRING
    )

    enum class ProfileScreenState {
        ACTOR_DATA_SUCCESS,
        ACTOR_SAVED_SUCCESS,
        ACTOR_SAVING_ERROR,
        SHOW_CREDIT_MOVIES,
        SHOW_LOADER,
        SERVICE_ERROR
    }
}
