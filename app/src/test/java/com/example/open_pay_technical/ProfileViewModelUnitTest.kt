package com.example.open_pay_technical

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.open_pay_technical.data.database.Database
import com.example.open_pay_technical.data.entity.Actor
import com.example.open_pay_technical.data.entity.Movie
import com.example.open_pay_technical.data.service.CombinedCreditsService
import com.example.open_pay_technical.data.service.MostPopularActorService
import com.example.open_pay_technical.data.service.MostPopularActorServiceImpl
import com.example.open_pay_technical.ui.profile.ProfileViewModel
import com.example.open_pay_technical.ui.profile.ProfileViewModel.*
import com.example.open_pay_technical.util.Constants.EMPTY_STRING
import com.example.open_pay_technical.util.Result
import io.mockk.MockKAnnotations
import io.mockk.MockKSettings.relaxed
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import io.realm.kotlin.Realm
import java.util.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ProfileViewModelUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: ProfileViewModel
    private val actor = Actor(ACTOR_ONE_ID, ACTOR_ONE_NAME, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING)

    private val mockedMostPopularActor: MostPopularActorService = mockkClass(MostPopularActorService::class)
    private val mockedCombinedCreditsService: CombinedCreditsService = mockkClass(CombinedCreditsService::class)

    private val mockedObserver = mockk<androidx.lifecycle.Observer<ProfileScreenData>>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init()

        viewModel = ProfileViewModel(mockedMostPopularActor, mockedCombinedCreditsService)
    }

    @After
    fun afterTests() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `Get most popular actors`() {
        val observerList = arrayListOf<ProfileScreenData>()
        val resultActor = Result.Success(actor)

        every { mockedMostPopularActor.getMostPopularActors() } returns resultActor

        viewModel.getLiveData().observeForever(mockedObserver)
        runBlocking {
            viewModel.fetchInitData().join()
        }

        verify { mockedObserver.onChanged(capture(observerList)) }

        assertEquals(ProfileScreenState.SHOW_LOADER, observerList[0].state)
        assertEquals(ProfileScreenState.GET_ACTOR_DATA_SUCCESS, observerList[1].state)
        assertEquals(resultActor.data, observerList[1].actor)
    }

    companion object {
        const val ACTOR_ONE_ID = "1"
        const val ACTOR_ONE_NAME = "Carlos"
    }
}
