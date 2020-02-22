package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import android.util.Log
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseFavoriteStop
import au.com.kbrsolutions.melbournepublictransport.testutils.getOrAwaitValue
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking

class FavoriteStopsRepositoryTests(
    val repository: FavoriteStopsRepository,
    val context: Context,
    val isRealRepo: Boolean) {

    fun getFavoriteStops() = runBlocking {
        repository.insert(embarcaderoFavoriteStop)
        val liveData = repository.getFavoriteStops()
        val lineStopDetailsList = liveData.getOrAwaitValue()
        Truth.assertThat(lineStopDetailsList.size).isEqualTo(1)
        Truth.assertThat(lineStopDetailsList[0].stopId).isEqualTo(embarcaderoStopId)
    }

    fun insert() = runBlocking {
        repository.insert(walnutCreekFavoriteStop)
        val insertedRow = repository.getFavoriteStop(walnutCreekId)
        Log.v(G_P + "FavoriteStopsRepositoryTests", """insert - insertedRow: ${insertedRow} """)
        Truth.assertThat(insertedRow!!.stopId).isEqualTo(walnutCreekStopId)
    }

    fun toggleMagnifiedView_insertRowAndToggle() = runBlocking {
        repository.insert(walnutCreekFavoriteStop)
        var insertedRow = repository.getFavoriteStop(walnutCreekId)
        Truth.assertThat(insertedRow!!.showInMagnifiedView).isFalse()
        repository.toggleMagnifiedView(walnutCreekId)
        insertedRow = repository.getFavoriteStop(walnutCreekId)
        Truth.assertThat(insertedRow!!.showInMagnifiedView).isTrue()
    }

    fun toggleMagnifiedView_insert3RowsToggle2ndDeleteToggleLast() = runBlocking {
        repository.insert(walnutCreekFavoriteStop)
        repository.insert(embarcaderoFavoriteStop)
        repository.insert(sopotFavoriteStop)

        repository.toggleMagnifiedView(embarcaderoId)
        var toggledRow = repository.getFavoriteStop(embarcaderoId)
        Truth.assertThat(toggledRow!!.showInMagnifiedView).isTrue()

        repository.deleteFavoriteStop(embarcaderoStopId)

        repository.toggleMagnifiedView(sopotId)
        toggledRow = repository.getFavoriteStop(sopotId)
        Truth.assertThat(toggledRow!!.showInMagnifiedView).isTrue()
    }

    fun toggleMagnifiedView_insert3RowsToggle2ndToggleLast() = runBlocking {
        repository.insert(walnutCreekFavoriteStop)
        repository.insert(embarcaderoFavoriteStop)
        repository.insert(sopotFavoriteStop)

        repository.toggleMagnifiedView(embarcaderoId)
        var toggledRow = repository.getFavoriteStop(embarcaderoId)
        Truth.assertThat(toggledRow!!.showInMagnifiedView).isTrue()

        repository.toggleMagnifiedView(sopotId)
        toggledRow = repository.getFavoriteStop(embarcaderoId)
        Truth.assertThat(toggledRow!!.showInMagnifiedView).isFalse()

        toggledRow = repository.getFavoriteStop(sopotId)
        Truth.assertThat(toggledRow!!.showInMagnifiedView).isTrue()
    }

    fun deleteFavoriteStop() = runBlocking {
        repository.insert(sopotFavoriteStop)
        repository.deleteFavoriteStop(sopotStopId)
        val deletedRow = repository.getFavoriteStop(sopotId)
        Log.v(G_P + "FavoriteStopsRepositoryTests", """deleteFavoriteStop - deletedRow: ${deletedRow} """)
        Truth.assertThat(deletedRow).isNull()
    }

    fun deleteAllFavoriteStops() = runBlocking {
        repository.insert(walnutCreekFavoriteStop)
        repository.insert(embarcaderoFavoriteStop)
        repository.insert(sopotFavoriteStop)
        Truth.assertThat(repository.getFavoriteStopsCount()).isEqualTo(3)

        repository.deleteAllFavoriteStops()
        Truth.assertThat(repository.getFavoriteStopsCount()).isEqualTo(0)
    }

    val embarcaderoId = 1
    val embarcaderoStopId = 1000
    val embarcaderoLocationName = "Embarcadero Station"
    val embarcaderoFavoriteStop = DatabaseFavoriteStop(
        embarcaderoId,
        0,
        embarcaderoStopId,
        embarcaderoLocationName,
        "San Francisco",
        1.1,
        2.2,
        false
    )

    val walnutCreekId = 2
    val walnutCreekStopId = 2000
    val walnutCreekLocationName = "WalnutCreek Station"
    val walnutCreekFavoriteStop = DatabaseFavoriteStop(
        walnutCreekId,
        0,
        walnutCreekStopId,
        walnutCreekLocationName,
        "WalnutCreek",
        1.1,
        2.2,
        false
    )

    val sopotId = 3
    val sopotStopId = 3000
    val sopotLocationName = "WalnutCreek Station"
    val sopotFavoriteStop = DatabaseFavoriteStop(
        sopotId,
        0,
        sopotStopId,
        sopotLocationName,
        "WalnutCreek",
        1.1,
        2.2,
        false
    )
}