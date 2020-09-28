package com.nandra.movieverse.viewmodel

/*
class SharedViewModel(val app: Application) : AndroidViewModel(app) {

    var isHomeDataHasLoaded: Boolean = false
    var isSearchDataLoaded: Boolean = false
    private var homeJob : Job? = null
    private var detailJob : Job? = null
    private var roomJob: Job? = null
    private var searchJob: Job? = null
    private var currentSearchKeyword = ""
    private var currentSearchType = ""
    private val repository = MyRepository(app)
    val detailState = MutableLiveData<Int>()
    val searchState = MutableLiveData<NetworkState>()
    val detailFilm: LiveData<DetailResponse>
        get() = _detailFilm
    private val _detailFilm = MutableLiveData<DetailResponse>()

    var movieFavoriteList = repository.getFavoriteMovieList()
    var tvFavoriteList = repository.getFavoriteTVList()

    val isHomeLoading: LiveData<Boolean>
        get() = _isHomeLoading
    val isHomeError: LiveData<Boolean>
        get() = _isHomeError
    val listTrendingLive: LiveData<ArrayList<Film>>
        get() = _listTrendingLive
    val listNowPlayingLive: LiveData<List<Film>>
        get() = _listNowPlayingLive
    val searchResultList: LiveData<List<Film>>
        get() = _searchResultList

    private val _searchResultList = MutableLiveData<List<Film>>()
    private val _isHomeLoading = MutableLiveData<Boolean>()
    private val _isHomeError = MutableLiveData<Boolean>()
    private val _listTrendingLive = MutableLiveData<ArrayList<Film>>()
    private val _listNowPlayingLive = MutableLiveData<List<Film>>()

    private val discoverMovieLiveData = MutableLiveData(repository.discoverMovieData(viewModelScope))
    val discoverMoviePagingListLiveData = Transformations.switchMap(discoverMovieLiveData) {it.pagedList}
    val discoverMovieNetworkStateLiveData = Transformations.switchMap(discoverMovieLiveData) {it.networkState}
    val discoverMovieIsInitialDataLoadedLiveData = Transformations.switchMap(discoverMovieLiveData) {it.initialState}

    private val discoverTVLiveData = MutableLiveData(repository.discoverTVData(viewModelScope))
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

    fun attemptSearch(keyword: String, type: String) {
        if (keyword == currentSearchKeyword && type == currentSearchType) {
            if (isSearchDataLoaded) {
                searchState.value = NetworkState.LOADED
            } else {
                if(isConnectedToInternet()) {
                    fetchSearchData(keyword, type)
                } else {
                    searchState.value = NetworkState.CANNOT_CONNECT
                }
            }
        } else if (keyword != currentSearchKeyword || type != currentSearchType){
            if (isConnectedToInternet()) {
                currentSearchKeyword = keyword
                currentSearchType = type
                fetchSearchData(keyword, type)
            } else {
                searchState.value = NetworkState.CANNOT_CONNECT
            }
        }
    }

    private fun fetchSearchData(keyword: String, type: String) {
        searchJob = viewModelScope.launch {
            searchState.postValue(NetworkState.LOADING)
            try {
                val response = if (type == Constant.MOVIE_FILM_TYPE) {
                    repository.searchMovie(keyword, 1)
                } else {
                    repository.searchTV(keyword, 1)
                }

                if (response.isSuccessful) {
                    val searchResult = response.body()?.results
                    searchResult?.run {
                        _searchResultList.postValue(this)
                        searchState.postValue(NetworkState.LOADED)
                        isSearchDataLoaded = true
                    }
                } else {
                    isSearchDataLoaded = false
                    searchState.postValue(NetworkState.FAILED)
                }
            } catch (exception: Exception) {
                isSearchDataLoaded = false
                searchState.postValue(NetworkState.CANNOT_CONNECT)
            }
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
        */
/*else
            detailState.value = Constant.STATE_NO_CONNECTION*//*

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
                    _detailFilm.postValue(film)
                    detailState.postValue(Constant.STATE_SUCCESS)
                } else {
                    detailState.postValue(Constant.STATE_SERVER_ERROR)
                }

            } catch (error: Exception){
                detailState.postValue(Constant.STATE_SERVER_ERROR)
            }
        }
    }

    fun saveToFavorite(data: DetailResponse, filmType: String) {
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
                        data.overview
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
                        data.overview
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
}*/
