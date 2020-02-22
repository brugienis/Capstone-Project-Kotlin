package au.com.kbrsolutions.melbournepublictransport.favoritestops

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import au.com.kbrsolutions.melbournepublictransport.MainCoroutineRule
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepositoryFake
import au.com.kbrsolutions.melbournepublictransport.testutils.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the implementation of [FavoriteStopsViewModelTest]
 */
@ExperimentalCoroutinesApi
class FavoriteStopsViewModelTest {
    private val walnutCreekLocationName = "Walnut Creek"
    private val walnutCreekStopId = 1000

    private val lafayetteLocationName = "Lafayette"
    private val lafayetteId = 1
    private val lafayetteStopId = 2000

    private val embarcaderoStopId = 3000
    private val sanFrancisco = "San Francisco"

    private val favoriteStopsList = listOf(
        FavoriteStop(
            0,
            0,
            walnutCreekStopId,
            walnutCreekLocationName,
            "Walnut Creek",
            1.1,
            2.2,
            false
        ),
        FavoriteStop(
            lafayetteId,
            0,
            lafayetteStopId,
            lafayetteLocationName,
            "Lafayette",
            1.1,
            2.2,
            false
        ),
        FavoriteStop(
            2,
            0,
            embarcaderoStopId,
            sanFrancisco,
            "San Francisco",
            1.1,
            2.2,
            false
        )
    )

    // Subject under test
    private lateinit var favoriteStopsViewModel: FavoriteStopsViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var repository: FavoriteStopsRepositoryFake

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        repository = FavoriteStopsRepositoryFake()
        repository.addFavoriteStops(*favoriteStopsList.toTypedArray())

        favoriteStopsViewModel = FavoriteStopsViewModel(repository)
    }

    @Test
    fun getFavoriteStops() {
        assertThat(favoriteStopsViewModel.favoriteStops.getOrAwaitValue()).hasSize(3)
    }

    @Test
    fun deleteOneFavoriteStop() {
        favoriteStopsViewModel.onDeleteFavoriteStop(lafayetteStopId)
        assertThat(favoriteStopsViewModel.favoriteStops.getOrAwaitValue()).hasSize(2)
    }

    @Test
    fun deleteAllFavoriteStops() {
        favoriteStopsViewModel.onClear()
        assertThat(favoriteStopsViewModel.favoriteStops.getOrAwaitValue()).hasSize(0)
    }

    @Test
    fun makeFavoriteStopMagnifiedAndNormalAgain() {
        // Magnify lafayetteId row
        favoriteStopsViewModel.onListViewClick(lafayetteId)
        var list: List<FavoriteStop> = favoriteStopsViewModel.favoriteStops.getOrAwaitValue()
        var lafayetteFavoriteStop = list[1]
        assertThat(lafayetteFavoriteStop.showInMagnifiedView).isTrue()

        // De-magnify lafayetteId row
        favoriteStopsViewModel.onListViewClick(lafayetteId)

        list = favoriteStopsViewModel.favoriteStops.getOrAwaitValue()
        lafayetteFavoriteStop = list[1]
        assertThat(lafayetteFavoriteStop.showInMagnifiedView).isFalse()
    }

    @Test
    fun showDeparturesClicked__toggleNavigateToNextDepartures() {
        favoriteStopsViewModel.onShowDeparturesClicked(favoriteStopsList[lafayetteId])
        assertThat(favoriteStopsViewModel.navigateToNextDepartures.getOrAwaitValue())
            .isEqualTo(favoriteStopsList[lafayetteId])

        favoriteStopsViewModel.doneNavigating()

        assertThat(favoriteStopsViewModel.navigateToNextDepartures.getOrAwaitValue()).isNull()
    }

}