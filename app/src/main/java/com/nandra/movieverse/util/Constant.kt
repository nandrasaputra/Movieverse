package com.nandra.movieverse.util

import com.nandra.movieverse.BuildConfig

object Constant {
    const val API_KEY_MOVIE_DB = BuildConfig.TMDB_API_KEY
    const val API_KEY_YANDEX = BuildConfig.YANDEX_API_KEY
    const val TV_FILM_TYPE = "tvshow"
    const val MOVIE_FILM_TYPE = "movie"
    const val LANGUAGE_ENGLISH_VALUE = "en-US"

    const val STATE_NO_CONNECTION = 1
    const val STATE_SERVER_ERROR = 2
    const val STATE_SUCCESS = 3
    const val STATE_LOADING = 4
    const val STATE_NOSTATE = 5
}