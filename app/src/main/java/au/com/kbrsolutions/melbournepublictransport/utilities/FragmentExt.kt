package au.com.kbrsolutions.melbournepublictransport.utilities

/**
 * Various extension functions for Fragment.
 */

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import au.com.kbrsolutions.melbournepublictransport.MptApplication
import au.com.kbrsolutions.melbournepublictransport.ViewModelFactory

const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>): T {
    Log.v("<top>", """obtainViewModel - applicationContext: ${requireContext().applicationContext} """)
    val repository = (requireContext().applicationContext as MptApplication).favoriteStopsRepository
    return ViewModelProviders.of(this, ViewModelFactory(repository)).get(viewModelClass)
}