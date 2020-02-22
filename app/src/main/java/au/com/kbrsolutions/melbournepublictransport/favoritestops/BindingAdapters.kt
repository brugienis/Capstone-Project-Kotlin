package au.com.kbrsolutions.melbournepublictransport.favoritestops

import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop

/*
    The BindingAdapter's value - 'transportImg' - has to be typed exactly as in the layout
        <ImageView
                android:id="@+id/favoriteStopsTransportImageId"
                .   .   .
                app:transportImg="@{favoriteStop}"
                      <|>
                .   .   . />
 */
@BindingAdapter("transportImg")
fun ImageView.setRouteImage(item: FavoriteStop?) {
    item?.let {
        setImageResource(when (item.routeType) {
            0 -> R.drawable.ic_stock_train_blue_500
            1 -> R.drawable.ic_stock_tram_amber_500
            2 -> R.drawable.ic_stock_directions_bus_green_500
            else -> R.drawable.ic_stock_train_blue_500
        })
    }
}

/**
 * If 'showView' is true, show fragment_favorite_stops_item_view.favoriteStopsIconsMagId
 * layout, otherwise hide it.
 */
@BindingAdapter("setTextSize")
fun TextView.setTextSize(showMagnified: Boolean) {
    this?.let {
        val sLocationNameViewMagnifiedTextSize = context.resources.getInteger(R.integer.list_view_line1_magnified_text_size_absolute)
        val sLocationNameViewTextSize = context.resources.getInteger(R.integer.list_view_line1_text_size_absolute)
        when (showMagnified) {
            true -> it.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                sLocationNameViewMagnifiedTextSize.toFloat())
            else -> it.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                sLocationNameViewTextSize.toFloat())
        }
    }
}
