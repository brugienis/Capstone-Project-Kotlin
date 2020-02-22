package au.com.kbrsolutions.melbournepublictransport.testsuites

import au.com.kbrsolutions.melbournepublictransport.activities.MainActivityTest
import au.com.kbrsolutions.melbournepublictransport.data.DepartureDaoTest
import au.com.kbrsolutions.melbournepublictransport.data.FavoriteStopDaoTest
import au.com.kbrsolutions.melbournepublictransport.departures.DeparturesFragmentTest
import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopsFragmentTest
import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopsFragmentTestUsingRoomDb
import au.com.kbrsolutions.melbournepublictransport.repository.*
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.StopsSearcherFragmentTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@ExperimentalCoroutinesApi
@Suite.SuiteClasses(
    MainActivityTest::class,

    FavoriteStopsFragmentTest::class,
    FavoriteStopDaoTest::class,
    FavoriteStopsFragmentTestUsingRoomDb::class,
    FavoriteStopsRepositoryRealTest::class,
    FavoriteStopsRepositoryFakeTest::class,


    DeparturesFragmentTest::class,
    DepartureDaoTest::class,
    DeparturesRepositoryRealTest::class,
    DeparturesRepositoryFakeTest::class,

    // fixLater: Feb 09, 2020 - StopsSearcherDaoTest missing
    StopsSearcherFragmentTest::class,
    StopsSearcherRepositoryRealTest::class,
    StopsSearcherRepositoryFakeTest::class
)
class TestCasesSuiteInstrumented