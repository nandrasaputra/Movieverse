package com.endiar.movieverse.core.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class FilmDetailResponse(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: Any,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int?,
    @SerializedName("imdb_id")
    val imdbId: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String?,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>,
    @SerializedName("release_date")
    val releaseDate: String?,
    val revenue: Int,
    val runtime: Int?,
    val status: String,
    val title: String?,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    val videos: Videos,
    val credits: Credits,
    @SerializedName("name")
    val tvTitle: String?,
    @SerializedName("first_air_date")
    val tvAirDate: String?,
    @SerializedName("number_of_episodes")
    val tvNumberOfEpisode: Int?,
    val images: Images
)

data class ProductionCompany(
    val id: Int,
    @SerializedName("logo_path")
    val logoPath: String,
    val name: String,
    @SerializedName("origin_country")
    val originCountry: String
)

data class Credits(
    val cast: List<Cast>
)

data class Videos(
    @SerializedName("results")
    val videoData: List<VideoData>
)

data class VideoData(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String
)

data class Images(
    val backdrops: List<Backdrops>
)

data class Backdrops(
    @SerializedName("file_path")
    val filepath: String
)

data class Cast(
    @SerializedName("cast_id")
    val castId: Int,
    val character: String?,
    @SerializedName("credit_id")
    val creditId: String,
    val gender: Int,
    val id: Int?,
    val name: String?,
    val order: Int,
    @SerializedName("profile_path")
    val profilePath: String?
)

data class Genre(
    val id: Int,
    val name: String
)