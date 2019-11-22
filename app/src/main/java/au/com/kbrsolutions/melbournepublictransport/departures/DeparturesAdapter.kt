package au.com.kbrsolutions.melbournepublictransport.departures

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import au.com.kbrsolutions.melbournepublictransport.databinding.FragmentDeparturesItemViewBinding
import au.com.kbrsolutions.melbournepublictransport.databinding.FragmentDeparturesItemViewMagnifiedBinding
import au.com.kbrsolutions.melbournepublictransport.domain.Departure

private const val ITEM_MAGNIFIED = 0
private const val ITEM = 1

class DeparturesAdapter(val clickListener: DepartureListener)
    : ListAdapter<Departure, RecyclerView.ViewHolder>(DepartureDiffCallback()) {

    // fixLater: Sep 02, 2019 - holders classes sealed?
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
            ITEM -> ViewHolderNormal.from(parent, viewType)
            else -> ViewHolderMagnified.from(parent, viewType)
        }
    }

    class ViewHolderNormal private constructor(val binding: FragmentDeparturesItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: DepartureListener, item: Departure) {
            binding.departure = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, viewType: Int): ViewHolderNormal {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentDeparturesItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolderNormal(binding)
            }
        }
    }

    class ViewHolderMagnified private constructor(val binding: FragmentDeparturesItemViewMagnifiedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: DepartureListener, item: Departure) {
            binding.departure = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, viewType: Int): ViewHolderMagnified {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentDeparturesItemViewMagnifiedBinding.inflate(layoutInflater, parent, false)
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
class DepartureDiffCallback : DiffUtil.ItemCallback<Departure>() {
    override fun areItemsTheSame(oldItem: Departure, newItem: Departure): Boolean {
        return oldItem.stopId == newItem.stopId
    }

    override fun areContentsTheSame(oldItem: Departure, newItem: Departure): Boolean {
        return oldItem == newItem
    }
}

class DepartureListener(val clickListener: (databaseDeparture: Departure, clickedViewName: String) -> Unit) {

    fun onClickNoIcon(databaseDeparture: Departure) =
        clickListener(databaseDeparture, LIST_VIEW_ROW)

    fun onDepartureLayoutId(databaseDeparture: Departure) =
        clickListener(databaseDeparture, DEPARTURE_LAYOUT_ID)

    fun onDisruptionsImageId(databaseDeparture: Departure) =
        clickListener(databaseDeparture, DISRUPTIONS_IMAGE_ID)

    fun onClickShowStopFacility(databaseDeparture: Departure) =
        clickListener(databaseDeparture, SHOW_STOP_FACILITY)

    companion object {
        const val LIST_VIEW_ROW = "listViewRow"
        const val DEPARTURE_LAYOUT_ID = "departureLayoutId"
        const val SHOW_STOP_FACILITY = "showStopFacility"
        const val DISRUPTIONS_IMAGE_ID = "disruptionsImageId"
    }
}
