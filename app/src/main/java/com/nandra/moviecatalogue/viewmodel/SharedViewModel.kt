package com.nandra.moviecatalogue.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nandra.moviecatalogue.database.FavoriteMovie
import com.nandra.moviecatalogue.database.FavoriteTV
import com.nandra.moviecatalogue.network.Film
import com.nandra.moviecatalogue.network.response.DetailResponse
import com.nandra.moviecatalogue.network.response.YandexResponse
import com.nandra.moviecatalogue.repository.MyRepository
import com.nandra.moviecatalogue.util.Constant
import com.nandra.moviecatalogue.util.getStringGenre
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SharedViewModel(val app: Application) : AndroidViewModel(app) {

    var currentLanguage: String = ""
    var isDataHasLoaded: Boolean = false
    private var discoverJob : Job? = null
    private var detailJob : Job? = null
    private var roomJob: Job? = null
    private val repository = MyRepository(app)
    var isOnDetailFragment = false

    val detailState = MutableLiveData<Int>()

    val detailFilm: LiveData<DetailResponse>
        get() = _detailFilm
    private val _detailFilm = MutableLiveData<DetailResponse>()

    val detailFilmTranslated: LiveData<YandexResponse>
        get() = _detailFilmTranslated
    private val _detailFilmTranslated = MutableLiveData<YandexResponse>()

    var movieFavoriteList = repository.getFavoriteMovieList()
    var tvFavoriteList = repository.getFavoriteTVList()

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

    val roomState = MutableLiveData<RoomState>().apply {
        this.value = RoomState.StandBy
    }

    suspend fun requestDiscoverData() {
        if(discoverJob != null){
            discoverJob?.join()
        }
        if (!isDataHasLoaded && isConnectedToInternet()) {
            fetchDiscoverData()
        } else if (!isDataHasLoaded && !isConnectedToInternet()) {
            _isError.value = true
        }
    }

    private suspend fun fetchDiscoverData() {
        _isLoading.value = true
        discoverJob = viewModelScope.launch {
            try {
                val movieResponse = repository.fetchDiscoverMovieResponse()
                val tvShowResponse = repository.fetchDiscoverTVSeriesResponse()

                if (movieResponse.isSuccessful && tvShowResponse.isSuccessful) {

                    val listMovie = movieResponse.body()?.results as ArrayList
                    val listTV = tvShowResponse.body()?.results as ArrayList

                    _listMovieLive.postValue(listMovie)
                    _listTVLive.postValue(listTV)

                    isDataHasLoaded = true
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

    suspend fun requestDetail(id: String, filmType: String) {
        if (isConnectedToInternet())
            fetchDetail(id, filmType)
        else
            detailState.value = Constant.STATE_NO_CONNECTION
    }

    fun deleteFavoriteMovie(movie: FavoriteMovie) {
        viewModelScope.launch {
            repository.removeFavorieMovie(movie)
        }
    }

    fun deleteFavoriteTV(tv: FavoriteTV) {
        viewModelScope.launch {
            repository.removeFavorieTV(tv)
        }
    }

    private suspend fun fetchDetail(id: String, filmType: String) {
        if (detailJob != null) {
            if (detailJob!!.isActive)     {
                detailJob!!.join()
            }
        }
        detailJob = viewModelScope.launch {
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

    fun saveToFavorite(
        data: DetailResponse,
        filmType: String,
        genreIndonesia: String,
        overviewIndonesia: String
    ) {
        roomJob = viewModelScope.launch {
            roomState.value = RoomState.Loading
            try {
                if(filmType == Constant.MOVIE_FILM_TYPE) {
                    val movie = FavoriteMovie(
                        data.id.toString(),
                        data.title,
                        data.posterPath,
                        data.backdropPath,
                        data.voteAverage.toString(),
                        filmType,
                        data.genres.getStringGenre(),
                        genreIndonesia,
                        data.overview,
                        overviewIndonesia
                    )
                    repository.saveMovieToFavorite(movie)
                    roomState.value = RoomState.Success
                    roomState.value = RoomState.StandBy
                } else {
                    val tv = FavoriteTV(
                        data.id.toString(),
                        data.tvTitle,
                        data.posterPath,
                        data.backdropPath,
                        data.voteAverage.toString(),
                        filmType,
                        data.genres.getStringGenre(),
                        genreIndonesia,
                        data.overview,
                        overviewIndonesia
                    )
                    repository.saveTVToFavorite(tv)
                    roomState.value = RoomState.Success
                    roomState.value = RoomState.StandBy
                }
            } catch (exp: Exception) {
                roomState.value = RoomState.Failure
                roomState.value = RoomState.StandBy
            }
        }
    }

    sealed class RoomState {
        object StandBy : RoomState()
        object Loading : RoomState()
        object Success : RoomState()
        object Failure : RoomState()
    }

}