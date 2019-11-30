package au.com.kbrsolutions.melbournepublictransport.utilities

/**
 * Copied from 'android-testing-codelab' project.
 *
 * Various extension functions for Fragment.
 */

import android.content.Context
import android.view.WindowManager.LayoutParams
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
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

/*
    Below from
        https://code.luasoftware.com/tutorials/android/android-hide-keyboard-on-activity-start/
 */
fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    // else {
    window.setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    // }
}

fun Fragment.hideKeyboard() {
    val activity = this.activity
    if (activity is AppCompatActivity) {
        activity.hideKeyboard()
    }
}