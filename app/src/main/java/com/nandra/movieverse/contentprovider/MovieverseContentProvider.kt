package com.nandra.movieverse.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.nandra.movieverse.database.MovieverseDatabase

class MovieverseContentProvider : ContentProvider() {

    init {
        uriMatcher.addURI(AUTHORITY, "movie", MOVIE)
        uriMatcher.addURI(AUTHORITY, "tv", TV)

    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val database = MovieverseDatabase.getInstance(context!!)
        return when(uriMatcher.match(uri)) {
            1 -> {
                database.favoriteMovieDao().getFavoriteMovieListCursor()
            }
            2 -> {
                database.favoriteTVDao().getFavoriteTVListCursor()
            }
            else -> {
                null
            }
        }
    }

    override fun onCreate(): Boolean { return true }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    companion object {
        const val AUTHORITY = "com.nandra.movieverse.contentprovider.favorite"
        const val MOVIE = 1
        const val TV = 2
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }
}