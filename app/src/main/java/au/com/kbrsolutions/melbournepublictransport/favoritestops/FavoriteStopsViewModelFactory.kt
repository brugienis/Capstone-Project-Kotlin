package au.com.kbrsolutions.melbournepublictransport.favoritestops

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepository

class FavoriteStopsViewModelFactory(private val favoriteStopsRepository: FavoriteStopsRepository)
    : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteStopsViewModel::class.java)) {
            return FavoriteStopsViewModel(favoriteStopsRepository) as T
        }
        throw IllegalArgumentException("FavoriteStopsViewModelFactory - Unknown ViewModel class")
    }
}
