package au.com.kbrsolutions.melbournepublictransport.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import au.com.kbrsolutions.melbournepublictransport.data.helper.TestDataGenerator
import au.com.kbrsolutions.melbournepublictransport.testutils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DepartureDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var departureDao: DepartureDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        departureDao = database.departureDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testInsertAndGetDeparture() {
        val departures = TestDataGenerator.generateDataDeparturesList1Row()
        departureDao.insert(departures)
        val retrievedDeparture = departureDao.getDeparture(1)
        ViewMatchers.assertThat(
            departures[0],
            CoreMatchers.equalTo(retrievedDeparture))
    }

    @Test
    fun testInsertAndGetDepartures() {
        val departures = TestDataGenerator.generateDataDeparturesList1Row()
        departureDao.insert(departures)
        val retrievedDepartures = departureDao.getDepartures(1L).getOrAwaitValue()
        retrievedDepartures.forEachIndexed { index, databaseDeparture ->
            ViewMatchers.assertThat(
                departures[index],
                CoreMatchers.equalTo(databaseDeparture))
        }
    }

    @Test
    fun clearTableAndInsertNewRows() {
        val departures = TestDataGenerator.generateDataDeparturesList1Row()
        departureDao.insert(departures)

        val departures3Rows = TestDataGenerator.generateDataDeparturesList3Rows()
        departureDao.clearTableAndInsertNewRows(departures3Rows)

        val retrievedDepartures = departureDao.getDepartures(1L).getOrAwaitValue()
        ViewMatchers.assertThat(
            retrievedDepartures.size,
            CoreMatchers.equalTo(3))
        retrievedDepartures.forEachIndexed { index, databaseDeparture ->
            ViewMatchers.assertThat(
                departures3Rows[index],
                CoreMatchers.equalTo(databaseDeparture))
        }
    }

    @Test
    fun toggleMagnifiedNormalView() {
        val departures3Rows = TestDataGenerator.generateDataDeparturesList3Rows()
        departureDao.clearTableAndInsertNewRows(departures3Rows)

        val retrievedDeparturesBefore= departureDao.getDepartures(1L).getOrAwaitValue()
        retrievedDeparturesBefore.forEach { databaseDeparture ->
            ViewMatchers.assertThat(
                databaseDeparture.showInMagnifiedView,
                CoreMatchers.equalTo(false))
        }

        val scndRowId = departures3Rows[1].id
        departureDao.toggleMagnifiedNormalView(scndRowId)

        val retrievedDeparturesAfter= departureDao.getDepartures(1L).getOrAwaitValue()
        var showInMagnifiedViewExpected: Boolean
        retrievedDeparturesAfter.forEachIndexed { index, databaseDeparture ->
            showInMagnifiedViewExpected = databaseDeparture.id == scndRowId
            ViewMatchers.assertThat(
                databaseDeparture.showInMagnifiedView,
                CoreMatchers.equalTo(showInMagnifiedViewExpected))
        }
    }

}