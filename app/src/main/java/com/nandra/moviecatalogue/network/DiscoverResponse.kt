package com.nandra.moviecatalogue.network


import com.google.gson.annotations.SerializedName

data class DiscoverResponse(
    val page: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val results: List<Film>
)