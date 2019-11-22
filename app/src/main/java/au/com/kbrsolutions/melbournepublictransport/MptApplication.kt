package au.com.kbrsolutions.melbournepublictransport

import android.app.Application
import au.com.kbrsolutions.melbournepublictransport.repository.DeparturesRepository
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepository

class MptApplication: Application() {

    // Depends on the flavor,
    val favoriteStopsRepository: FavoriteStopsRepository
        get() = ServiceLocator.provideFavoriteStopsRepository(this)
    val departuresRepository: DeparturesRepository
        get() = ServiceLocator.provideDeparturersRepository(this)
}
