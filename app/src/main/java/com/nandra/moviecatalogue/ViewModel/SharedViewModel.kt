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
    var listMovies: ArrayList<Film> = arrayListOf()
    var listTVSeries: ArrayList<Film> = arrayListOf()
    var isDataHasLoaded: Boolean = false
    private var job = Job()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError

    init {
        _isLoading.value = false
        _isError.value = false
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
                    listMovies = movieResponse.body()?.results as ArrayList
                    listTVSeries = tvShowResponse.body()?.results as ArrayList
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

    suspend fun getListMovie() : ArrayList<Film> {
        return if(!isDataHasLoaded && isConnectedToInternet()) {
            fetchData()
            listMovies
        } else if (!isDataHasLoaded && !isConnectedToInternet()) {
            _isError.value = true
            listMovies
        } else {
            _isLoading.value = false
            listMovies
        }
    }

    suspend fun getListTVSeries() : ArrayList<Film> {
        return if(!isDataHasLoaded && isConnectedToInternet()) {
            fetchData()
            listTVSeries
        } else if (!isDataHasLoaded && !isConnectedToInternet()) {
            _isError.value = true
            listTVSeries
        } else {
            _isLoading.value = false
            listTVSeries
        }
    }

}