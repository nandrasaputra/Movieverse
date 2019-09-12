package com.nandra.moviecatalogue.network.response


import com.google.gson.annotations.SerializedName
import com.nandra.moviecatalogue.network.Film

data class DiscoverResponse(
    val page: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val results: List<Film>
)