package au.com.kbrsolutions.melbournepublictransport.favoritestops

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import au.com.kbrsolutions.melbournepublictransport.MainCoroutineRule
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepositoryFake
import au.com.kbrsolutions.melbournepublictransport.testutils.getLiveDataValue
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
    private val walnutCreek = "Walnut Creek"
    private val lafayette = "Lafayette"
    private val lafayetteId = 1
    private val lafayetteStopId = "Lafayette Station"
    private val sanFrancisco = "San Francisco"
    private val favoriteStopsList = listOf(
        FavoriteStop(
            0,
            0,
            "Walnut Station",
            walnutCreek,
            "Walnut Creek",
            1.1,
            2.2,
            false
        ),
        FavoriteStop(
            lafayetteId,
            0,
            lafayetteStopId,
            lafayette,
            "Lafayette",
            1.1,
            2.2,
            false
        ),
        FavoriteStop(
            2,
            0,
            "Embarcadero Station",
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
        assertThat(getLiveDataValue(favoriteStopsViewModel.favoriteStops)).hasSize(3)
    }

    @Test
    fun deleteOneFavoriteStop() {
        favoriteStopsViewModel.onDeleteFavoriteStop(lafayetteStopId)
        assertThat(getLiveDataValue(favoriteStopsViewModel.favoriteStops)).hasSize(2)
    }

    @Test
    fun deleteAllFavoriteStops() {
        favoriteStopsViewModel.onClear()
        assertThat(getLiveDataValue(favoriteStopsViewModel.favoriteStops)).hasSize(0)
    }

    @Test
    fun makeFavoriteStopMagnifiedAndNormalAgain() {
        // Magnify lafayetteId row
        favoriteStopsViewModel.onListViewClick(lafayetteId)
        var list: List<FavoriteStop> = getLiveDataValue(favoriteStopsViewModel.favoriteStops)
        var lafayetteFavoriteStop = list[1]
        assertThat(lafayetteFavoriteStop.showInMagnifiedView).isTrue()

        // De-magnify lafayetteId row
        favoriteStopsViewModel.onListViewClick(lafayetteId)

        list = getLiveDataValue(favoriteStopsViewModel.favoriteStops)
        lafayetteFavoriteStop = list[1]
        assertThat(lafayetteFavoriteStop.showInMagnifiedView).isFalse()
    }

    @Test
    fun showDeparturesClicked__toggleNavigateToNextDepartures() {
        assertThat(getLiveDataValue(favoriteStopsViewModel.navigateToNextDepartures)).isNull()

        favoriteStopsViewModel.onShowDeparturesClicked(favoriteStopsList[lafayetteId])
        assertThat(getLiveDataValue(favoriteStopsViewModel.navigateToNextDepartures))
            .isEqualTo(favoriteStopsList[lafayetteId])

        favoriteStopsViewModel.doneNavigating()

        assertThat(getLiveDataValue(favoriteStopsViewModel.navigateToNextDepartures)).isNull()
    }

}