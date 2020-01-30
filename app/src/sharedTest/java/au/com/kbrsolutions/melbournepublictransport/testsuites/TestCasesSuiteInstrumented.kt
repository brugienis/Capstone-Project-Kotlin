package au.com.kbrsolutions.melbournepublictransport.testsuites

import au.com.kbrsolutions.melbournepublictransport.activities.MainActivityTest
import au.com.kbrsolutions.melbournepublictransport.data.DepartureDaoTest
import au.com.kbrsolutions.melbournepublictransport.data.FavoriteStopDaoTest
import au.com.kbrsolutions.melbournepublictransport.departures.DeparturesFragmentTest
import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopsFragmentTest
import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopsFragmentTestUsingRoomDb
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.StopsSearcherFragmentTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MainActivityTest::class,

    FavoriteStopsFragmentTest::class,
    FavoriteStopsFragmentTestUsingRoomDb::class,

    FavoriteStopDaoTest::class,

    DeparturesFragmentTest::class,
    DepartureDaoTest::class,

    StopsSearcherFragmentTest::class
)
class TestCasesSuiteInstrumented