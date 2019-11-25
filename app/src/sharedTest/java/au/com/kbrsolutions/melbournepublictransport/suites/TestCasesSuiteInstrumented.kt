package au.com.kbrsolutions.melbournepublictransport.suites

import au.com.kbrsolutions.melbournepublictransport.data.DepartureDaoTest
import au.com.kbrsolutions.melbournepublictransport.data.FavoriteStopDaoTest
import au.com.kbrsolutions.melbournepublictransport.departures.DeparturesFragmentTest
import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopsFragmentTest
import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopsFragmentTestUsingRoomDb
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    FavoriteStopsFragmentTest::class,
    FavoriteStopsFragmentTestUsingRoomDb::class,

    FavoriteStopDaoTest::class,

    DeparturesFragmentTest::class,
    DepartureDaoTest::class
)
class TestCasesSuiteInstrumented