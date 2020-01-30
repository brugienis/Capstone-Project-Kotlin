package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import au.com.kbrsolutions.melbournepublictransport.databinding.FragmentStopsSearcherItemViewBinding
import au.com.kbrsolutions.melbournepublictransport.databinding.FragmentStopsSearcherItemViewMagnifiedBinding
import au.com.kbrsolutions.melbournepublictransport.departures.DepartureListener
import au.com.kbrsolutions.melbournepublictransport.domain.LineStopDetails

private const val ITEM_MAGNIFIED = 0
private const val ITEM = 1

class StopsSearcherAdapter(val clickListener: StopsSearcherListener)
    : ListAdapter<LineStopDetails, RecyclerView.ViewHolder>(stopsSearcherDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val departureDetails = getItem(position)
        when (holder) {
            is ViewHolderNormal -> {
                holder.bind(clickListener, departureDetails)
            }

            is ViewHolderMagnified -> {
                holder.bind(clickListener, departureDetails)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).showInMagnifiedView) {
            true -> ITEM_MAGNIFIED
            else -> ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM -> ViewHolderNormal.from(
                parent,
                viewType
            )
            else -> ViewHolderMagnified.from(parent, viewType)
        }
    }

    class ViewHolderNormal private constructor(val binding: FragmentStopsSearcherItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: StopsSearcherListener, item: LineStopDetails) {
            binding.lineStopDetails = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, viewType: Int): ViewHolderNormal {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FragmentStopsSearcherItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolderNormal(binding)
            }
        }
    }

    class ViewHolderMagnified private constructor(val binding: FragmentStopsSearcherItemViewMagnifiedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: StopsSearcherListener, item: LineStopDetails) {
            binding.lineStopDetails = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, viewType: Int): ViewHolderMagnified {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentStopsSearcherItemViewMagnifiedBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return ViewHolderMagnified(binding)
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
class stopsSearcherDiffCallback : DiffUtil.ItemCallback<LineStopDetails>() {
    override fun areItemsTheSame(oldItem: LineStopDetails, newItem: LineStopDetails): Boolean {
        return oldItem.lineId == newItem.lineId
    }

    override fun areContentsTheSame(oldItem: LineStopDetails, newItem: LineStopDetails): Boolean {
        return oldItem == newItem
    }
}

class StopsSearcherListener(val clickListener: (
    lineStopDetails: LineStopDetails,
    clickedViewName: String) -> Unit) {

    fun onClickNoIcon(lineStopDetails: LineStopDetails) =
        clickListener(lineStopDetails, DepartureListener.LIST_VIEW_ROW)

    fun onClickOnAddToFavoriteOrGetStopsOnLine(lineStopDetails: LineStopDetails) =
        clickListener(lineStopDetails, ADD_TO_FAVORITE_OR_GET_STOPS_ONLINE_ID)

    companion object {
        const val LIST_VIEW_ROW = "listViewRow"
        const val ADD_TO_FAVORITE_OR_GET_STOPS_ONLINE_ID = "ddToFavoriteOrGetStopsOnLine"
    }
}