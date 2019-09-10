package com.nandra.moviecatalogue.network


data class YandexResponse(
    val code: Int,
    val lang: String,
    val text: List<String>
)