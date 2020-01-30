package au.com.kbrsolutions.melbournepublictransport.testsuites

import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopsViewModelTest
import au.com.kbrsolutions.melbournepublictransport.network.PtvServiceUtilsTest
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.StopsSearcherJsonProcessorTest
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.StopsSearcherViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    PtvServiceUtilsTest::class,
    FavoriteStopsViewModelTest::class,
    StopsSearcherViewModelTest::class,
    StopsSearcherJsonProcessorTest::class
)
class TestCasesSuiteJvm