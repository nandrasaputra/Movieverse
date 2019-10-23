package com.nandra.movieverse.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.*
import com.nandra.movieverse.data.Listing
import com.nandra.movieverse.database.FavoriteMovie
import com.nandra.movieverse.database.FavoriteTV
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.network.response.DetailResponse
import com.nandra.movieverse.network.response.YandexResponse
import com.nandra.movieverse.repository.MyRepository
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.util.getStringGenre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SharedViewModel(val app: Application) : AndroidViewModel(app) {

    var currentLanguage: String = ""
    var isHomeDataHasLoaded: Boolean = false
    private var homeJob : Job? = null
    private var detailJob : Job? = null
    private var roomJob: Job? = null
    private val repository = MyRepository(app)
    val isOnDetailFragment = MutableLiveData<Boolean>().apply {
        this.value = false
    }

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
    val isHomeLoading: LiveData<Boolean>
        get() = _isHomeLoading
    val isError: LiveData<Boolean>
        get() = _isError
    val isHomeError: LiveData<Boolean>
        get() = _isHomeError
    val listTrendingLive: LiveData<ArrayList<Film>>
        get() = _listTrendingLive
    val listNowPlayingLive: LiveData<List<Film>>
        get() = _listNowPlayingLive

    private val _searchResult = MutableLiveData<ArrayList<Film>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isHomeLoading = MutableLiveData<Boolean>()
    private val _isError = MutableLiveData<Boolean>()
    private val _isHomeError = MutableLiveData<Boolean>()
    private val _listTrendingLive = MutableLiveData<ArrayList<Film>>()
    private val _listNowPlayingLive = MutableLiveData<List<Film>>()

    private val discoverMovieLiveData = MutableLiveData<Listing<Film>>(repository.discoverMovieData(viewModelScope))
    val discoverMoviePagingListLiveData = Transformations.switchMap(discoverMovieLiveData) {it.pagedList}
    val discoverMovieNetworkStateLiveData = Transformations.switchMap(discoverMovieLiveData) {it.networkState}
    val discoverMovieIsInitialDataLoadedLiveData = Transformations.switchMap(discoverMovieLiveData) {it.initialState}

    private val discoverTVLiveData = MutableLiveData<Listing<Film>>(repository.discoverTVData(viewModelScope))
    val discoverTVPagingListLiveData = Transformations.switchMap(discoverTVLiveData) {it.pagedList}
    val discoverTVNetworkStateLiveData = Transformations.switchMap(discoverTVLiveData) {it.networkState}
    val discoverTVIsInitialDataLoadedLiveData = Transformations.switchMap(discoverTVLiveData) {it.initialState}

    val roomState = MutableLiveData<RoomState>().apply {
        this.value = RoomState.StandBy
    }

    fun retryLoadAllFailed() {
        if (isConnectedToInternet()) {
            discoverMovieLiveData.value?.retry?.invoke()
            discoverTVLiveData.value?.retry?.invoke()
        }
    }

    suspend fun requestHomeData() {
        if(homeJob != null){
            homeJob?.join()
        }
        if (!isHomeDataHasLoaded && isConnectedToInternet()) {
            fetchHomeData()
        } else if (!isHomeDataHasLoaded && !isConnectedToInternet()) {
            _isHomeError.value = true
        }
    }

    private suspend fun fetchHomeData() {
        _isHomeLoading.value = true
        homeJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val trendingResponse = repository.fetchTrendingList()
                val nowPlayingResponse = repository.fetchNowPlayingList()
                if(trendingResponse.isSuccessful && nowPlayingResponse.isSuccessful) {
                    val listTrending = trendingResponse.body()?.results as ArrayList
                    val listNowPlaying = nowPlayingResponse.body()?.results
                    _listTrendingLive.postValue(listTrending)
                    _listNowPlayingLive.postValue(listNowPlaying)
                    isHomeDataHasLoaded = true
                    _isHomeLoading.postValue(false)
                    _isHomeError.postValue(false)
                } else {
                    _isHomeLoading.postValue(false)
                    _isHomeError.postValue(true)
                }
            } catch (exp: Exception){
                _isHomeLoading.postValue(false)
                _isHomeError.postValue(true)
            }
        }
        homeJob?.join()
    }

    @Suppress("DEPRECATION")
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
            repository.removeFavoriteMovie(movie)
        }
    }

    fun deleteFavoriteTV(tv: FavoriteTV) {
        viewModelScope.launch {
            repository.removeFavoriteTV(tv)
        }
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

    suspend fun attemptSearch(query: String, type: String, page: Int = 1) {

    }

    private suspend fun searchMovie(query: String, page: Int) {

    }

    private suspend fun searchTV(query: String, page: Int) {

    }

    fun saveToFavorite(data: DetailResponse, filmType: String, genreIndonesia: String, overviewIndonesia: String) {
        roomJob = viewModelScope.launch(Dispatchers.IO) {
            roomState.postValue(RoomState.Loading)
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
                    roomState.postValue(RoomState.Success)
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
                    roomState.postValue(RoomState.Success)
                }
            } catch (exp: Exception) {
                roomState.postValue(RoomState.Failure)
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