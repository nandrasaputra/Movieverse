package com.nandra.moviecatalogue.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nandra.moviecatalogue.network.DetailResponse
import com.nandra.moviecatalogue.network.Film
import com.nandra.moviecatalogue.network.YandexResponse
import com.nandra.moviecatalogue.repository.MyRepository
import com.nandra.moviecatalogue.util.Constant
import com.nandra.moviecatalogue.util.getStringGenre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SharedViewModel(val app: Application) : AndroidViewModel(app) {

    var currentLanguage: String = ""
    var isDataHasLoaded: Boolean = false
    private var discoverJob : Job? = null
    private var detailJob : Job? = null
    private val repository = MyRepository(app)
    var isOnDetailFragment = false

    val detailState = MutableLiveData<Int>()

    val detailFilm: LiveData<DetailResponse>
        get() = _detailFilm
    private val _detailFilm = MutableLiveData<DetailResponse>()

    val detailFilmTranslated: LiveData<YandexResponse>
        get() = _detailFilmTranslated
    private val _detailFilmTranslated = MutableLiveData<YandexResponse>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading
    val isError: LiveData<Boolean>
        get() = _isError
    val listMovieLive: LiveData<ArrayList<Film>>
        get() = _listMovieLive
    val listTVLive: LiveData<ArrayList<Film>>
        get() = _listTVLive

    private val _isLoading = MutableLiveData<Boolean>()
    private val _listMovieLive = MutableLiveData<ArrayList<Film>>()
    private val _isError = MutableLiveData<Boolean>()
    private val _listTVLive = MutableLiveData<ArrayList<Film>>()

    suspend fun requestDiscoverData(language: String) {
        if(discoverJob != null){
            discoverJob?.join()
        }

        if(isNewLanguage(language)) {
            if (isConnectedToInternet()) {
                fetchData(language)
            } else {
                _isError.value = true
            }
        } else {
            if (!isDataHasLoaded && isConnectedToInternet()) {
                fetchData(language)
            } else if (!isDataHasLoaded && !isConnectedToInternet()) {
                _isError.value = true
            }
        }
    }

    private suspend fun fetchData(language: String) {
        _isLoading.value = true
        discoverJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieResponse = repository.fetchMovieResponse(language)
                val tvShowResponse = repository.fetchTVSeriesResponse(language)

                if (movieResponse.isSuccessful && tvShowResponse.isSuccessful) {

                    val listMovie = movieResponse.body()?.results as ArrayList
                    val listTV = tvShowResponse.body()?.results as ArrayList

                    _listMovieLive.postValue(listMovie)
                    _listTVLive.postValue(listTV)

                    isDataHasLoaded = true
                    currentLanguage = language
                    _isLoading.postValue(false)
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
        discoverJob?.join()
    }

    private fun isConnectedToInternet() : Boolean {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun isNewLanguage(language: String) : Boolean {
        return (language != currentLanguage)
    }

    suspend fun requestDetail(id: String, filmType: String) {
        if (isConnectedToInternet())
            fetchDetail(id, filmType)
        else
            detailState.value = Constant.STATE_NO_CONNECTION
    }

    private suspend fun fetchDetail(id: String, filmType: String) {
        if (detailJob != null) {
            if (detailJob!!.isActive)     {
                detailJob!!.join()
            }
        }
        detailJob = viewModelScope.launch(Dispatchers.IO) {
            detailState.postValue(Constant.STATE_LOADING)
            try {
                val response = if (filmType == Constant.MOVIE_FILM_TYPE) {
                    repository.fetchMovieDetailResponse(id)
                } else {
                    repository.fetchTVDetailResponse(id)
                }

                if (response.isSuccessful) {
                    val film = response.body()
                    val translateResponse = repository.translateText(listOf(film!!.overview, film.genres.getStringGenre()))
                    if (translateResponse.isSuccessful) {
                        val translatedFilm = translateResponse.body()
                        _detailFilm.postValue(film)
                        _detailFilmTranslated.postValue(translatedFilm)
                        detailState.postValue(Constant.STATE_SUCCESS)
                    } else {
                        detailState.postValue(Constant.STATE_SERVER_ERROR)
                    }
                } else {
                    detailState.postValue(Constant.STATE_SERVER_ERROR)
                }

            } catch (error: Exception){
                detailState.postValue(Constant.STATE_SERVER_ERROR)
            }
        }
    }

}