package au.com.kbrsolutions.melbournepublictransport.departures

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import au.com.kbrsolutions.melbournepublictransport.repository.DeparturesRepository

class DeparturesViewModelFactory(
    private val stopId: String,
    private val locationName: String,
    private val favoriteStopsRequestedTimMillis: Long,
    private val context: Context,
    private val departuresRepository: DeparturesRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeparturesViewModel::class.java)) {
            return DeparturesViewModel(stopId, locationName, favoriteStopsRequestedTimMillis,
                context, departuresRepository) as T
        }
        throw IllegalArgumentException("DeparturesViewModelFactory - unknown ViewModel class")
    }
}
