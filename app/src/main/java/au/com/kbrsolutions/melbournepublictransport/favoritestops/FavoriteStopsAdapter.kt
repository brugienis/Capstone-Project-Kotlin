package au.com.kbrsolutions.melbournepublictransport.favoritestops

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import au.com.kbrsolutions.melbournepublictransport.databinding.FragmentFavoriteStopsItemViewBinding
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop

class FavoriteStopsAdapter(val clickListener: FavoriteStopListener)
    : ListAdapter<FavoriteStop, FavoriteStopsAdapter.ViewHolder>(SleepNightDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: FragmentFavoriteStopsItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: FavoriteStopListener, item: FavoriteStop) {
            binding.favoriteStop = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentFavoriteStopsItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class SleepNightDiffCallback : DiffUtil.ItemCallback<FavoriteStop>() {
    override fun areItemsTheSame(oldItem: FavoriteStop, newItem: FavoriteStop): Boolean {
        return oldItem.stopId == newItem.stopId
    }

    override fun areContentsTheSame(oldItem: FavoriteStop, newItem: FavoriteStop): Boolean {
        return oldItem == newItem
    }
}

class FavoriteStopListener(val clickListener: (favoriteStop: FavoriteStop, clickedViewName: String) -> Unit) {

    fun onClickNoIcon(favoriteStop: FavoriteStop) = clickListener(favoriteStop, LIST_VIEW_ROW)

    fun onClickRemoveStop(favoriteStop: FavoriteStop) =
        clickListener(favoriteStop, DELETE_FAVORITE_STOP)

    fun onClickShowStopOnMap(favoriteStop: FavoriteStop) =
        clickListener(favoriteStop, SHOW_STOP_ON_MAP)

    fun onClickDepartures(favoriteStop: FavoriteStop) = clickListener(favoriteStop, DEPARTURES)

    fun onClickShowStopFacility(favoriteStop: FavoriteStop) =
        clickListener(favoriteStop, SHOW_STOP_FACILITY)

    companion object {
        const val LIST_VIEW_ROW = "listViewRow"
        const val DELETE_FAVORITE_STOP = "deleteFavoriteStop"
        const val SHOW_STOP_ON_MAP = "showStopOnMap"
        const val DEPARTURES = "departures"
        const val SHOW_STOP_FACILITY = "showStopFacility"
    }
}
