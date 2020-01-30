package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView.OnEditorActionListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.databinding.FragmentStopsSearcherBinding
import au.com.kbrsolutions.melbournepublictransport.domain.LineStopDetails
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.StopsSearcherListener.Companion.ADD_TO_FAVORITE_OR_GET_STOPS_ONLINE_ID
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.StopsSearcherListener.Companion.LIST_VIEW_ROW
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import au.com.kbrsolutions.melbournepublictransport.utilities.hideKeyboard
import au.com.kbrsolutions.melbournepublictransport.utilities.obtainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class StopsSearcherFragment : Fragment() {

    private lateinit var stopsSearcherViewModel: StopsSearcherViewModel
    private lateinit var stopsSearchText: TextInputEditText
    private lateinit var adapter: StopsSearcherAdapter
    private var searchTextChangedByUser = true
//    private lateinit var progressBarHandler: ProgressBarHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding: FragmentStopsSearcherBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stops_searcher, container, false)

        val stopsSearchTextLayout = binding.stopsSearchTextLayout

        stopsSearchText = binding.stopsSearchText
        stopsSearchText.requestFocus()

        stopsSearchText.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            when (stopsSearcherViewModel.handleSearchButtonClicked(v, actionId, getSearchText())) {
                true -> {
                    setOnTouchListenerOnStopsSearchText()
                    true
                }
                else -> false
            }
        })

        stopsSearchTextLayout.setEndIconOnClickListener(View.OnClickListener {
            stopsSearcherViewModel.clearSearchTextClicked()
        })

        setOnTextChangedOnStopsSearchText()

//        val application = requireNotNull(this.activity).application

        stopsSearcherViewModel = obtainViewModel(StopsSearcherViewModel::class.java, null)

        binding.stopsSearcherViewModel = stopsSearcherViewModel

        adapter = StopsSearcherAdapter(StopsSearcherListener{ lineStopDetails, clickedViewName ->
            Log.v(G_P + "StopsSearcherFragment", """onCreateView - lineStopDetails: ${lineStopDetails} """)
            when (clickedViewName) {
                LIST_VIEW_ROW -> stopsSearcherViewModel.onListItemViewClick(lineStopDetails.id)
                ADD_TO_FAVORITE_OR_GET_STOPS_ONLINE_ID -> {
                    stopsSearcherViewModel.onAddStopOrGetStopsOnlineClicked(lineStopDetails.id)
                }
            }
        })

        binding.linesStopsSearchResults.adapter = adapter

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        // fixLater: Jan 16, 2020 - show progress bar when search started - should be initiated
        //                          in the view model by some flag observed in this fragment
        /*progressBarHandler = ProgressBarHandler(this, binding.root)
        progressBarHandler.show()*/

        stopsSearcherViewModel.loadErrMsg.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showErrorMsg(binding.root, it)
//                progressBarHandler.hide()
            }
//            EspressoIdlingResource.decrement("StopsSearcherViewModel.startLineAndStopsSearch")
        })

        stopsSearcherViewModel.navigateUp.observe(viewLifecycleOwner, Observer {
            if (it) {
                stopsSearcherViewModel.navigateUpDone()
//                requireActivity().onNavigateUp()
                this.findNavController().navigate(
                    StopsSearcherFragmentDirections
                        .actionStopsSearcherFragmentToFavoriteStopsFragment())
                Log.v(G_P + "StopsSearcherFragment", """onCreateView after onNavigate - it: ${it} """)
            }
        })

        stopsSearcherViewModel.stopsSearchResults.observe(viewLifecycleOwner, Observer {
            val currentTimeMillis = System.currentTimeMillis()
            println(G_P + """StopsSearcherFragment - onCreateView topsSearchResults.observe - it.size: ${it.size}""")
            it?.let {
                //                if (it.isNotEmpty()) {
//                    progressBarHandler.hide()
//                    val departuresSorted = departureViewModel.sortDepartures(
//                        it, SharedPreferencesUtility.isSortDeparturesDataByTime(context!!))
                updateAdapterData(it)
                if (it.isNotEmpty()) {
                    stopsSearcherViewModel.setShownView(ShowView.SearchResults)
                }
//                }
            }
        })

//        stopsSearcherViewModel.insertFavoriteStop()

        return binding.root
    }

    private fun updateAdapterData(lineStopDetails: List<LineStopDetails>) {
        Log.v(G_P + "StopsSearcherFragment", """updateAdapterData - lineStopDetails: ${lineStopDetails.size} """)
        activity!!.runOnUiThread {
            adapter.submitList(lineStopDetails)
            Log.v(G_P + "StopsSearcherFragment", """updateAdapterData after submit - lineStopDetails: ${lineStopDetails.size} """)
        }
    }

    private fun showErrorMsg(binding: View, errorMsg: String) {
        Snackbar.make(binding, errorMsg, Snackbar.LENGTH_SHORT).run {
            show()
        }
    }

    // Cancel current stops search when user touches the stopsSearchText
    @SuppressLint("LongLogTag", "ClickableViewAccessibility")

    private fun setOnTouchListenerOnStopsSearchText() {
        stopsSearchText.setOnTouchListener(View.OnTouchListener {view, motionEvent ->
            Log.v(G_P + "StopsSearcherFragment", """setOnTouchListenerOnStopsSearchText - motionEvent: ${motionEvent.action} """)
            when (motionEvent.action) {
                MotionEvent.ACTION_UP -> {
                    handleTouchEvent()
                    true
                }
                else -> false
            }
        })
    }

    private fun setOnTextChangedOnStopsSearchText() {
        stopsSearchText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                handleSearchTextChanged(s)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // There is nothing to do here
            }

            override fun afterTextChanged(s: Editable) { // There is nothing to do here
            }
        })
    }

    private var mMostRecentTimeTextChanged = -1L
    private var mSearchTextChangedByUser = true
    private val mMinTextLgthForAutoSearch = 3

    private fun handleSearchTextChanged(s: CharSequence) {
        if (mSearchTextChangedByUser) {
            /*if (s.toString().length == 0) {
                mClearSearchTextBtn.setVisibility(View.INVISIBLE)
            } else {
                mClearSearchTextBtn.setVisibility(View.VISIBLE)
            }
            if (mConfigurationChanged) {
                return
            }*/
            if (s.toString().length >= mMinTextLgthForAutoSearch) {
                mMostRecentTimeTextChanged = System.currentTimeMillis()
                autoStartSearchChecker("handleSearchTextChanged")
            }
        } else {
            mSearchTextChangedByUser = true
        }
    }

    private var handler = Handler()
    private val WAIT_MILLIS_BEFORE_START_AUTO_SEARCH = 1000

    private var autoStartSearchRunnable: AutoStartSearchRunnable? = null

    private fun autoStartSearchChecker(source: String) {
        if (autoStartSearchRunnable != null) { // remove just in case if there is already one waiting in a queue
            handler.removeCallbacks(autoStartSearchRunnable)
        }
        autoStartSearchRunnable = AutoStartSearchRunnable("$source.autoStartSearchChecker")
        handler.postDelayed(
            autoStartSearchRunnable,
            WAIT_MILLIS_BEFORE_START_AUTO_SEARCH.toLong()
        )
    }

    /**
     * Runnable that will start each time user changed search text.
     */
    inner class AutoStartSearchRunnable internal constructor(source: String?) :
        Runnable {
        /**
         * If user did not change search text for WAIT_MILLIS_BEFORE_START_AUTO_SEARCH, starts
         * search, otherwise post delay a new AutoStartSearchRunnable.
         */
        override fun run() {
            if (System.currentTimeMillis() - mMostRecentTimeTextChanged >
                WAIT_MILLIS_BEFORE_START_AUTO_SEARCH
            ) {
                val searchTest: String? = getSearchText()
                searchTest?.let {
                    setOnTouchListenerOnStopsSearchText()
                    hideKeyboard()
                    stopsSearcherViewModel.validateSearchTextAndStartSearch(it)
                }
            } else {
                autoStartSearchChecker("AutoStartSearchRunnable.run")
            }
        }
    }

    /**
     *
     */
    private fun handleTouchEvent() {
        hideKeyboard()
        stopsSearchText.setOnTouchListener(null)
        Log.v(G_P + "StopsSearcherFragment", """handleTouchEvent - setOnTouchListener set to null""")
        stopsSearcherViewModel.cancelStopsSearch()
//        setVisibleView(mSearchInfoTextTV, "handleTouchEvent")
        // In the future add code to start search for 'recent searches' texts
//        mListener.startSearchForRecentSearchesText()
    }

    // fixLater: Jan 30, 2020 - investigate why I added the below
    private fun showInfoText(text: String) {
        stopsSearchText.setText(text)
    }

    /**
     * Use that method to get text from mStopsSearchTextET view.
     *
     * @param searchText
     */
    private fun getSearchText(): String? {
        val searchText: String = stopsSearchText.text.toString().trim {
            it <= ' ' }
        return if (searchText.isEmpty()) {
            null
        } else searchText
        return searchText
    }

    /**
     * Remove search results before going back to the previous fragment.
     */
    override fun onStop() {
        super.onStop()
        stopsSearcherViewModel.clearRepositoryTables()
        Log.v(G_P + "StopsSearcherFragment", """onStop - called""")
    }

    /**
     * Use that method to set a new text in stopsSearchText view.
     *
     * The OnEditorActionListener.onTextChanged will ignore those changes and will only process
     * changes done by a user changing text.
     *
     * @param searchText
     */
    /*private fun setStopsSearchText(searchText: String?) {
        searchTextChangedByUser = false
        if (searchText != null) {
            stopsSearchText.setText(searchText)
            stopsSearchText.setSelection(searchText.length)
        }
    }*/

}
