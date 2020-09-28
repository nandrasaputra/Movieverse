package com.endiar.movieverse.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tv")
data class FavoriteTVEntity (
    @PrimaryKey val tvId: Int,
    val title: String,
    val posterPath: String,
    val backdropPath: String,
    val voteAverage: Double,
    val filmType: String,
    val genre: String,
    val overview: String
) : Film