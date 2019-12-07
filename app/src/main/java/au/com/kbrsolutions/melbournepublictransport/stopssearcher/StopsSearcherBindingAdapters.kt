package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import au.com.kbrsolutions.melbournepublictransport.R

/**
 * This binding adapter toggles the 'show less/more' icon.
 */
@BindingAdapter(value = ["showHideInstructionsIcon", "source"], requireAll = true)
fun setShowHideInstructionsIcon(
    iconView: ImageView,
    showInstructions: Boolean,
    source: String?) {
    val context = iconView.context
    if (showInstructions) {
        iconView.contentDescription = context.getText(R.string.text_show_less)
        iconView.setImageResource(R.drawable.ic_stock_expand_more_green_500)
    } else {
        iconView.contentDescription = context.getText(R.string.text_show_more)
        iconView.setImageResource(R.drawable.ic_stock_expand_less_green_500)
    }
}

/**
 * This binding adapter shows/hides instructions for the Stops Searcher screen.
 */
@BindingAdapter(value = ["searchTextInstructions", "source1"])
fun showHideSearchTextInstructions(
    searchInstructionsView: TextView,
    showInstructions: Boolean,
    source: String?) {
    if (showInstructions) {
        searchInstructionsView.visibility = View.GONE
    } else {
        searchInstructionsView.visibility = View.VISIBLE
    }
}