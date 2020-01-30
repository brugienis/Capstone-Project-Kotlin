package au.com.kbrsolutions.melbournepublictransport

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import au.com.kbrsolutions.melbournepublictransport.departures.DeparturesFragmentArgs
import au.com.kbrsolutions.melbournepublictransport.departures.DeparturesViewModel
import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopsViewModel
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.StopsSearcherViewModel

/**
 * A creator is used to inject the product ID into the ViewModel
 *
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
class ViewModelFactory constructor(private val arguments: Any?, val context: Context)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            val mptApp = context.applicationContext as MptApplication
            when {
                isAssignableFrom(FavoriteStopsViewModel::class.java) ->
                    FavoriteStopsViewModel(
                        mptApp.favoriteStopsRepository)

                isAssignableFrom(DeparturesViewModel::class.java) -> {
                    DeparturesViewModel(
                        arguments as DeparturesFragmentArgs,
                        context,
                        mptApp.departuresRepository
                    )
                }

                isAssignableFrom(StopsSearcherViewModel::class.java) ->
                    StopsSearcherViewModel(
                        mptApp.favoriteStopsRepository,
                        mptApp.stopsSearcherRepository,
                        context)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
