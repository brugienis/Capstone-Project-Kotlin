package au.com.kbrsolutions.melbournepublictransport.utilities

/**
 * Copied from 'android-testing-codelab' project.
 *
 * Various extension functions for Fragment.
 */

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import au.com.kbrsolutions.melbournepublictransport.ViewModelFactory

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>, arguments: Any?): T {
    val context = requireContext()

    return ViewModelProviders.of(
        this,
        ViewModelFactory(arguments, context)
    ).get(viewModelClass)
}