package com.nandra.moviecatalogue.network.response


import com.google.gson.annotations.SerializedName
import com.nandra.moviecatalogue.network.*

data class DetailResponse(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: Any,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    @SerializedName("imdb_id")
    val imdbId: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>,
    @SerializedName("release_date")
    val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    val status: String,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    val videos: Videos,
    val credits: Credits,
    @SerializedName("name")
    val tvTitle: String,
    @SerializedName("first_air_date")
    val tvAirDate: String,
    @SerializedName("number_of_episodes")
    val tvNumberOfEpisode: String,
    val images: Images
)