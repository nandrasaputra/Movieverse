package com.nandra.movieverse.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.DiscoverPagedListAdapter
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.util.NetworkState
import com.nandra.movieverse.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_discover_tv.*

class DiscoverTVFragment : Fragment() {

    private var currentLanguage: String = ""
    private lateinit var languageEnglishValue : String
    private lateinit var preferenceLanguageKey : String
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var discoverTVPagedListAdapter: DiscoverPagedListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover_tv, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.discoverTVPagingListLiveData.observe(this, Observer {
            discoverTVPagedListAdapter.submitList(it)
        })
        sharedViewModel.discoverTVNetworkStateLiveData.observe(this, Observer {
            discoverTVPagedListAdapter.setNetworkState(it)
            handleNetworkState(it)
        })
        sharedViewModel.discoverTVIsInitialDataLoadedLiveData.observe(this, Observer {  })
        discoverTVPagedListAdapter = DiscoverPagedListAdapter(Constant.TV_FILM_TYPE) {sharedViewModel.retryLoadAllFailed()}
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        discover_tv_recyclerview.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = discoverTVPagedListAdapter
        }
        checkErrorState()
        viewErrorLanguageAdjustment()
        sharedViewModel.discoverTVNetworkStateLiveData.value?.run {
            handleNetworkState(this)
        }
    }

    private fun handleNetworkState(state: NetworkState) {

        val isInitialDataLoaded = sharedViewModel.discoverTVIsInitialDataLoadedLiveData.value ?: false

        when(state) {
            NetworkState.LOADING -> {
                if(!isInitialDataLoaded){
                    discover_tv_error_back.visibility = View.GONE
                    discover_tv_progress_bar.visibility = View.VISIBLE
                    discover_tv_cover.visibility = View.VISIBLE
                }
            }
            NetworkState.LOADED -> {
                if (isInitialDataLoaded){
                    discover_tv_progress_bar.visibility = View.GONE
                    discover_tv_cover.visibility = View.GONE
                }
            }
            NetworkState.FAILED -> {
                discover_tv_progress_bar.visibility = View.GONE
                if(!isInitialDataLoaded) {
                    discover_tv_error_back.visibility = View.VISIBLE
                    viewErrorLanguageAdjustment()
                    discover_tv_error_button.setOnClickListener {
                        sharedViewModel.retryLoadAllFailed()
                    }
                } else {
                    if (currentLanguage == languageEnglishValue)
                        Toast.makeText(activity, "Cannot Get Data From Server", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(activity, "Data Tidak Bisa Diambil Dari Server", Toast.LENGTH_SHORT).show()
                }
            }
            NetworkState.CANNOT_CONNECT -> {
                discover_tv_progress_bar.visibility = View.GONE
                if(!isInitialDataLoaded) {
                    discover_tv_error_back.visibility = View.VISIBLE
                    viewErrorLanguageAdjustment()
                    discover_tv_error_button.setOnClickListener {
                        sharedViewModel.retryLoadAllFailed()
                    }
                } else {
                    if (currentLanguage == languageEnglishValue)
                        Toast.makeText(activity, "Cannot Connect To Server", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(activity, "Tidak Dapat Terhubung Ke Server", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun prepareSharedPreferences() {
        preferenceLanguageKey = getString(R.string.preferences_language_key)
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        currentLanguage = sharedPreferences.getString(preferenceLanguageKey,
            languageEnglishValue)!!
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.detailState.value = Constant.STATE_NOSTATE
        sharedViewModel.isOnDetailFragment.value = false
    }

    private fun checkErrorState() {
        if (sharedViewModel.discoverTVIsInitialDataLoadedLiveData.value != null && sharedViewModel.discoverTVNetworkStateLiveData.value != null) {
            if (!sharedViewModel.discoverTVIsInitialDataLoadedLiveData.value!! && (sharedViewModel.discoverTVNetworkStateLiveData.value!! == NetworkState.FAILED
                        || sharedViewModel.discoverTVNetworkStateLiveData.value!! == NetworkState.CANNOT_CONNECT))
            {
                discover_tv_error_back.visibility = View.VISIBLE
                discover_tv_error_button.setOnClickListener {
                    sharedViewModel.retryLoadAllFailed()
                }
            }
        }
    }

    private fun viewErrorLanguageAdjustment() {
        if (currentLanguage == languageEnglishValue) {
            discover_tv_error_button.text = getString(R.string.button_try_again_en)
            discover_tv_error_text.text = getString(R.string.no_internet_connection_en)
        }
        else {
            discover_tv_error_button.text = getString(R.string.button_try_again_id)
            discover_tv_error_text.text = getString(R.string.no_internet_connection_id)
        }
    }
}