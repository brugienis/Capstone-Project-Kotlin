package au.com.kbrsolutions.melbournepublictransport.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import au.com.kbrsolutions.melbournepublictransport.testutils.getOrAwaitValue
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavoriteStopDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var favoriteStopDao: FavoriteStopDao

    private val favoriteStopA = DatabaseFavoriteStop(1, 1, 1, "loc A", "sub A", 1.0, 10.0)
    private val favoriteStopB = DatabaseFavoriteStop(2, 2, 2, "loc B", "sub B", 2.0, 20.0)
    private val favoriteStopC = DatabaseFavoriteStop(3, 3, 3, "loc C", "sub C", 3.0, 3.0)

    val favoriteStops = listOf(favoriteStopA, favoriteStopB, favoriteStopC)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
//        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        favoriteStopDao = database.favoriteStopDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testInsertAndGetFavoriteStop() {
        val favoriteStop = favoriteStopA
        favoriteStopDao.insert(favoriteStop)
//        ViewMatchers.assertThat(favoriteStopDao.getFavoriteStop(1).getOrAwaitValue(),
        ViewMatchers.assertThat(favoriteStopDao.getFavoriteStop(1),
            CoreMatchers.equalTo(favoriteStopA))
    }

    @Test
    fun testDeleteFavoriteStop() {
        insertFavoriteStops()
        ViewMatchers.assertThat(favoriteStopDao.getFavoriteStops().getOrAwaitValue().size, CoreMatchers.equalTo(3))
        favoriteStopDao.deleteFavoriteStop(1)
        val favoriteStops =  favoriteStopDao.getFavoriteStops()
        ViewMatchers.assertThat(favoriteStopDao.getFavoriteStops().getOrAwaitValue().size, CoreMatchers.equalTo(2))
    }

    private fun insertFavoriteStops() {
        favoriteStops.forEach {
            favoriteStopDao.insert(it)
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetFavoriteStopNoLiveData() {
        val favoriteStop = DatabaseFavoriteStop(1, 1, 1, "loc A", "sub A", 1.0, 10.0)
        favoriteStopDao.insert(favoriteStop)
        val testFavoriteStopFromDb = favoriteStopDao.getTestFavoriteStop(1)
        Assert.assertEquals( 1, testFavoriteStopFromDb.stopId)
    }

    @Test
    fun toggleMagnifiedNormalViewXxx() {
        println(G_P + " FavoriteStopDaoTest - toggleMagnifiedNormalViewXxx - start")
        runBlockingTest {
            val favoriteStop0 = DatabaseFavoriteStop(0, 1, 1, "loc A", "sub A", 1.0, 10.0)
            val favoriteStop1 = DatabaseFavoriteStop(1, 1, 2, "loc B", "sub B", 1.1, 11.0, true)
            val favoriteStop2 = DatabaseFavoriteStop(2, 1, 3, "loc C", "sub C", 1.2, 2.0)
            favoriteStopDao.insert(favoriteStop0)
            favoriteStopDao.insert(favoriteStop1)
            favoriteStopDao.insert(favoriteStop2)
            val testFavoriteStopFromDbBefore = favoriteStopDao.getTestFavoriteStop(1)
            Assert.assertEquals(1, testFavoriteStopFromDbBefore.stopId)
            Assert.assertEquals(false, testFavoriteStopFromDbBefore.showInMagnifiedView)
            val testFavoriteStopFromDbBefore1 = favoriteStopDao.getTestFavoriteStop(2)
            Assert.assertEquals(2, testFavoriteStopFromDbBefore1.stopId)
            Assert.assertEquals(true, testFavoriteStopFromDbBefore1.showInMagnifiedView)

            favoriteStopDao.toggleMagnifiedNormalView(0)

            val testFavoriteStopFromDbAfter = favoriteStopDao.getTestFavoriteStop(1)
//            Assert.assertEquals(1, testFavoriteStopFromDbAfter.getOrAwaitValue().stopId)
            Assert.assertEquals(1, testFavoriteStopFromDbAfter.stopId)
//            Assert.assertEquals(true, testFavoriteStopFromDbAfter.getOrAwaitValue().showInMagnifiedView)
            Assert.assertEquals(true, testFavoriteStopFromDbAfter.showInMagnifiedView)

            val testFavoriteStopFromDbAfter1 = favoriteStopDao.getTestFavoriteStop(2)
            Assert.assertEquals(2, testFavoriteStopFromDbAfter1.stopId)
            Assert.assertEquals(false, testFavoriteStopFromDbAfter1.showInMagnifiedView)
        }
    }

//    @Test
    fun toggleMagnifiedNormalView() {
        runBlockingTest {
            val favoriteStop0 = DatabaseFavoriteStop(0, 1, 1, "loc A", "sub A", 1.0, 10.0)
            val favoriteStop1 = DatabaseFavoriteStop(1, 1, 2, "loc B", "sub B", 1.1, 11.0)
            val favoriteStop2 = DatabaseFavoriteStop(2, 1, 3, "loc C", "sub C", 1.2, 2.0)
            favoriteStopDao.insert(favoriteStop0)
            favoriteStopDao.insert(favoriteStop1)
            favoriteStopDao.insert(favoriteStop2)
            val testFavoriteStopFromDbBefore = favoriteStopDao.getTestFavoriteStop(1)
            Assert.assertEquals(1, testFavoriteStopFromDbBefore.stopId)
            Assert.assertEquals(false, testFavoriteStopFromDbBefore.showInMagnifiedView)
            favoriteStopDao.toggleMagnifiedNormalView(0)
            val testFavoriteStopFromDbAfter = favoriteStopDao.getTestFavoriteStop(1)
            Assert.assertEquals(1, testFavoriteStopFromDbAfter.stopId)
            Assert.assertEquals(true, testFavoriteStopFromDbAfter.showInMagnifiedView)
        }
    }

}

/*
//    private var _liveDataFavoriteStopFromDb = MutableLiveData<FavoriteStop>()
//    private val liveDataFavoriteStopFromDb: LiveData<FavoriteStop>
//        get() = _liveDataFavoriteStopFromDb

//    @Test
//    @Throws(Exception::class)
//    fun insertAndGetFavoriteStopLiveData() {
//        val favoriteStop = DatabaseFavoriteStop(1, 1, 1, "loc A", "sub A", 1.0, 10.0)
//        favoriteStopDao.insert(favoriteStop)
//        Thread.sleep(2000)
//        _liveDataFavoriteStopFromDb = favoriteStopDao.getFavoriteStop(1)
//        Thread.sleep(2000)
//        Assert.assertEquals( 1, liveDataFavoriteStopFromDb.value?.stopId)
//    }
*/