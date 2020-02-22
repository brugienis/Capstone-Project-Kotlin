package au.com.kbrsolutions.melbournepublictransport.common

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment

/**
 * Indeterminate Progress Bar.
 */
class ProgressBarHandler(fragment: Fragment, view: View) {
    private val mProgressBar: ProgressBar

    init {
        val context: Context? = fragment.context
        val layout = view as ViewGroup

        mProgressBar = ProgressBar(context, null, android.R.attr.progressBarStyleLarge)
        mProgressBar.isIndeterminate = true

        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        val rl = RelativeLayout(context)

        rl.gravity = Gravity.CENTER
        rl.addView(mProgressBar)

        layout.addView(rl, params)

        hide()
    }

    fun show() {
        mProgressBar.visibility = View.VISIBLE
    }

    fun hide() {
        mProgressBar.visibility = View.INVISIBLE
    }
}
