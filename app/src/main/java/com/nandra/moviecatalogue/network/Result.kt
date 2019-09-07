package com.nandra.moviecatalogue.network


import com.google.gson.annotations.SerializedName

data class Result(
    val id: String,
    @SerializedName("iso_639_1")
    val iso6391: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String
)