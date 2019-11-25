package au.com.kbrsolutions.melbournepublictransport.testsuites

import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopsViewModelTest
import au.com.kbrsolutions.melbournepublictransport.network.PtvServiceUtilsTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    FavoriteStopsViewModelTest::class,
    PtvServiceUtilsTest::class
)
class TestCasesSuiteJvm