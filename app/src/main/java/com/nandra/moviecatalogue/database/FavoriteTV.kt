package com.nandra.moviecatalogue.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tv")
data class FavoriteTV (
    @PrimaryKey val id: String,
    val title: String,
    val posterPath: String,
    val backdropPath: String,
    val rating: String,
    val filmType: String,
    val genreEnglish: String,
    val genreIndonesia: String,
    val overviewEnglish: String,
    val overviewIndonesia: String
) : FilmData()