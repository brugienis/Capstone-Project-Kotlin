package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.databinding.FragmentStopsSearcherBinding
import au.com.kbrsolutions.melbournepublictransport.utilities.obtainViewModel

class StopsSearcherFragment : Fragment() {

    private lateinit var stopsSearcherViewModel: StopsSearcherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentStopsSearcherBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stops_searcher, container, false)

        val application = requireNotNull(this.activity).application

        stopsSearcherViewModel =obtainViewModel(StopsSearcherViewModel::class.java, null)

        binding.stopsSearcherViewModel = stopsSearcherViewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.setLifecycleOwner(this)

        setHasOptionsMenu(true)

//        stopsSearcherViewModel.insertFavoriteStop()

        return binding.root
    }
}
