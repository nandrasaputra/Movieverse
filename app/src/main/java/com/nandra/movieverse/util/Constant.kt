package com.nandra.movieverse.util

import com.nandra.movieverse.BuildConfig

object Constant {
    const val API_KEY_MOVIE_DB = BuildConfig.TMDB_API_KEY
    const val API_KEY_YOUTUBE = BuildConfig.GOOGLE_YOUTUBE_API
    const val TV_FILM_TYPE = "tv"
    const val MOVIE_FILM_TYPE = "movie"
    const val EXTRA_YOUTUBE_KEY = "extra_key"

    const val STATE_NO_CONNECTION = 1
    const val STATE_SERVER_ERROR = 2
    const val STATE_SUCCESS = 3
    const val STATE_LOADING = 4
    const val STATE_NOSTATE = 5

    const val PREFERENCE_KEY_TODAY_RELEASES = "today"
    const val PREFERENCE_KEY_REMINDER = "daily"

    const val NOTIFICATION_REMINDER_REQUEST_CODE = 101
    const val NOTIFICATION_TODAY_RELEASE_REQUEST_CODE = 102
    const val NOTIFICATION_REMINDER_ID = 111
    const val NOTIFICATION_DAILY_RELEASE_ID = 112
    const val NOTIFICATION_CHANNEL_ID = "general_notification"
    const val NOTIFICATION_CHANNEL_NAME = "Movieverse General Notification"
}