package com.nandra.moviecatalogue.ViewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nandra.moviecatalogue.network.Film
import com.nandra.moviecatalogue.repository.MyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SharedViewModel(val app: Application) : AndroidViewModel(app) {

    private val repository = MyRepository(app)
    var isDataHasLoaded: Boolean = false
    private var job = Job()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError

    private val _listMovieLive = MutableLiveData<ArrayList<Film>>()
    val listMovieLive: LiveData<ArrayList<Film>>
        get() = _listMovieLive
    private val _listTVLive = MutableLiveData<ArrayList<Film>>()
    val listTVLive: LiveData<ArrayList<Film>>
        get() = _listTVLive

    init {
        _isLoading.value = false
        _isError.value = false
        _listMovieLive.value = arrayListOf()
    }

    private fun isConnectedToInternet() : Boolean {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private suspend fun fetchData() {
        _isLoading.value = true
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieResponse = repository.fetchMovieResponse()
                val tvShowResponse = repository.fetchTVSeriesResponse()
                if (movieResponse.isSuccessful && tvShowResponse.isSuccessful) {
                    _listMovieLive.postValue(movieResponse.body()?.results as ArrayList)
                    _listTVLive.postValue(tvShowResponse.body()?.results as ArrayList)
                    _isLoading.postValue(false)
                    isDataHasLoaded = true
                    _isError.postValue(false)
                } else {
                    _isLoading.postValue(false)
                    _isError.postValue(true)
                }
            } catch (exp: Exception) {
                _isLoading.postValue(false)
                _isError.postValue(true)
            }
        }
        job.join()
    }

    suspend fun getListMovie() {
        return if(!isDataHasLoaded && isConnectedToInternet()) {
            fetchData()
        } else if (!isDataHasLoaded && !isConnectedToInternet()) {
            _isError.value = true
        } else {
            _isLoading.value = false
        }
    }

    suspend fun getListTVSeries() {
        return if(!isDataHasLoaded && isConnectedToInternet()) {
            fetchData()
        } else if (!isDataHasLoaded && !isConnectedToInternet()) {
            _isError.value = true
        } else {
            _isLoading.value = false
        }
    }

}