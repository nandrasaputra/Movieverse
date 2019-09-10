package com.nandra.moviecatalogue.network

import com.google.gson.annotations.SerializedName


data class GoogleCloud (
    @SerializedName("q") val word: List<String>,
    @SerializedName("target") val targetLanguage: String
)