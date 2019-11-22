package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepository

class StopsSearcherViewModelFactory(private val favoriteStopsRepository: FavoriteStopsRepository)
    : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StopsSearcherViewModel::class.java)) {
            return StopsSearcherViewModel(favoriteStopsRepository) as T
        }
        throw IllegalArgumentException("StopsSearcherViewModelFactory - Unknown ViewModel class")
    }
}