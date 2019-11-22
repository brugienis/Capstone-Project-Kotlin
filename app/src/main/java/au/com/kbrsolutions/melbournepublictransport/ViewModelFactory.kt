package au.com.kbrsolutions.melbournepublictransport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopsViewModel
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepository

/**
 * A creator is used to inject the product ID into the ViewModel
 *
 *
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
class ViewModelFactory constructor(
    private val tasksRepository: FavoriteStopsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(FavoriteStopsViewModel::class.java) ->
                    FavoriteStopsViewModel(tasksRepository)
                /*isAssignableFrom(TaskDetailViewModel::class.java) ->
                    TaskDetailViewModel(favoriteStopsRepository)
                isAssignableFrom(AddEditTaskViewModel::class.java) ->
                    AddEditTaskViewModel(favoriteStopsRepository)
                isAssignableFrom(TasksViewModel::class.java) ->
                    TasksViewModel(favoriteStopsRepository)*/
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
