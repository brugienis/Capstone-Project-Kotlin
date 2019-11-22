package au.com.kbrsolutions.melbournepublictransport.departures

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import au.com.kbrsolutions.melbournepublictransport.domain.Departure

/**
 * This adapter makes the  view visible/invisible depending on the values of 'loadingErrorMsg'
 * and 'showWhenLoadErrMsgIsNull'.
 *
 * 'source' is used for debugging only.
 */
@BindingAdapter(value = ["loadError", "showWhenLoadErrMsgIsNull", "source"], requireAll = true)
fun bindViewLoadErrMsg(
    view: View,
    loadingErrorMsg: String?,
    showWhenLoadErrMsgIsNull: Boolean,
    source: String) {
    when (loadingErrorMsg) {
        null -> view.visibility = if (showWhenLoadErrMsgIsNull) View.VISIBLE else View.INVISIBLE
        else -> view.visibility = if (!showWhenLoadErrMsgIsNull) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }
}

/**
 * This binding adapter displays the [DeparturesApiStatus] of the network request in an Text view.
 */
@BindingAdapter("runTypeOrLineShortName")
fun runTypeOrLineShortName(line1TextView: TextView, departure: Departure) {
    when (departure.runType) {
        "0" -> line1TextView.text = departure.runType
        else -> departure.lineShortName
    }
}