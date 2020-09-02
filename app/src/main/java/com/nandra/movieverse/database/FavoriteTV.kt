package com.nandra.movieverse.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tv")
data class FavoriteTV (
    @PrimaryKey val id: String,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val rating: String,
    val filmType: String,
    val genreEnglish: String,
    val overviewEnglish: String
) : FilmData()