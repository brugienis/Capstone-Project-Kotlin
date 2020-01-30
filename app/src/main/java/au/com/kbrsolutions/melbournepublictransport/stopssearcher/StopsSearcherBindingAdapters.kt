package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.domain.LineStopDetails
import au.com.kbrsolutions.melbournepublictransport.utilities.TRAIN_ROUTE_TYPE
import au.com.kbrsolutions.melbournepublictransport.utilities.TRAM_ROUTE_TYPE
import com.google.android.material.textfield.TextInputEditText

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
@BindingAdapter(value = ["searchTextInstructions", "source"])
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

/**
 * This binding adapter shows/hides instructions for the Stops Searcher screen.
 */
@SuppressLint("LongLogTag")
@BindingAdapter(value = ["clearSearchText", "source"])
fun clearSearchText(
    searchText: TextInputEditText,
    clearMsgClicked: Boolean?,
    source: String?) {
    if (clearMsgClicked != null && clearMsgClicked) {
        searchText.text = null
    }
}

/**
 * This binding adapter shows/hides instructions for the Stops Searcher screen.
 */
@BindingAdapter(value = ["searchValidationMsgVisibility", "source"])
fun setSearchValidationMsgVisibility(
    searchValidationMsgView: TextView,
    searchValidationMsg: String,
    source: String?) {
    if (searchValidationMsg.isEmpty()) {
        searchValidationMsgView.visibility = View.GONE
    } else {
        searchValidationMsgView.visibility = View.VISIBLE
    }
}

/**
 * This binding adapter shows the stop or line name.
 */
@BindingAdapter(value = ["stopOrLineName", "source"])
fun setStopOrLineName(
    stopSearcherStopOrLineNameName: TextView,
    lineStopDetails: LineStopDetails,
    source: String?) {
    if (lineStopDetails.routeType == TRAIN_ROUTE_TYPE) {
        stopSearcherStopOrLineNameName.text = lineStopDetails.stopLocationOrLineName
    } else if (lineStopDetails.lineOrStopType == LineStopDetails.LINE_DETAILS) {
            stopSearcherStopOrLineNameName.text = lineStopDetails.lineNumber
    } else {
        stopSearcherStopOrLineNameName.text = lineStopDetails.stopLocationOrLineName
    }
}

/**
 * This binding adapter 'add stop' or 'search for line stops' icon.
 */
@BindingAdapter(value = ["addStopOrFindLineStops", "source"])
fun addStopOrFindLineStops(
    stopSearcherAddToFavoriteOrGetStopsOnLineImageId: ImageView,
    lineStopDetails: LineStopDetails,
    source: String?) {
    val context = stopSearcherAddToFavoriteOrGetStopsOnLineImageId.context
    if (lineStopDetails.lineOrStopType == LineStopDetails.LINE_DETAILS) {
        stopSearcherAddToFavoriteOrGetStopsOnLineImageId.contentDescription =
            context.getText(R.string.content_desc_search_for_line_stops)
        stopSearcherAddToFavoriteOrGetStopsOnLineImageId.
            setImageResource(R.drawable.ic_stock_search_green_500)
    } else {
        stopSearcherAddToFavoriteOrGetStopsOnLineImageId.contentDescription =
            context.getText(R.string.content_desc_add_to_favorites)
        stopSearcherAddToFavoriteOrGetStopsOnLineImageId.
            setImageResource(R.drawable.ic_stock_add_circle_outline_green_500)
    }
}

/**
 * This binding adapter sets the 'transport image' icon.
 */
@BindingAdapter(value = ["transportImage", "source"])
fun setTransportImage(
    stopSearcherTransportImageId: ImageView,
    lineStopDetails: LineStopDetails,
    source: String?) {
    val context = stopSearcherTransportImageId.context
    when (lineStopDetails.routeType) {
        TRAIN_ROUTE_TYPE -> {
            stopSearcherTransportImageId.setImageResource(R.drawable.ic_stock_train_blue_500)
            stopSearcherTransportImageId.contentDescription =
                context.getText(R.string.content_desc_train_transport_type)
        }
        TRAM_ROUTE_TYPE -> {
            stopSearcherTransportImageId.setImageResource(R.drawable.ic_stock_tram_amber_500)
            stopSearcherTransportImageId.contentDescription =
                context.getText(R.string.content_desc_tram_transport_type)
        }
        else -> {
            stopSearcherTransportImageId.
                setImageResource(R.drawable.ic_stock_directions_bus_green_500)
            stopSearcherTransportImageId.contentDescription =
                context.getText(R.string.content_desc_bus_transport_type)
        }
    }
}

/**
 * This binding adapter sets the 'stopSearcherStopSuburbOrLineInfo' text.
 */
@BindingAdapter(value = ["stopSearcherStopSuburbOrLineInfo", "source"])
fun setStopSearcherStopSuburbOrLineInfo(
    stopSearcherStopSuburbOrLineInfo: TextView,
    lineStopDetails: LineStopDetails,
    source: String?) {
    if (lineStopDetails.routeType == TRAIN_ROUTE_TYPE) {
        stopSearcherStopSuburbOrLineInfo.text = lineStopDetails.suburb
    } else if (lineStopDetails.lineOrStopType == LineStopDetails.LINE_DETAILS) {
        stopSearcherStopSuburbOrLineInfo.text = lineStopDetails.lineNameShort
    } else {
        stopSearcherStopSuburbOrLineInfo.text = lineStopDetails.suburb
    }
}