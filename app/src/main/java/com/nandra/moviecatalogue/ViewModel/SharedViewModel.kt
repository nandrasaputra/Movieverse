package com.nandra.moviecatalogue.ViewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nandra.moviecatalogue.R
import com.nandra.moviecatalogue.data.Film
import com.nandra.moviecatalogue.repository.MyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(val app: Application) : AndroidViewModel(app) {

    var currentLanguage = app.getString(R.string.preferences_language_value_english)
    val repository = MyRepository(app)
    var listMoviez: ArrayList<Film> = arrayListOf()
    var isDataHasLoaded: Boolean = false

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

    suspend fun getListMovie() : ArrayList<Film> {
        return if(!isDataHasLoaded && isConnectedToInternet()) {
            _isLoading.value = true
            val job = viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = repository.fetchMovieResponse()
                    if (response.isSuccessful) {
                        listMoviez = response.body()?.results as ArrayList
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
            listMoviez
        } else if (!isDataHasLoaded && !isConnectedToInternet()) {
            _isError.value = true
            listMoviez
        } else {
            _isLoading.value = false
            listMoviez
        }
    }

    fun getListTVSeries() {

    }

}