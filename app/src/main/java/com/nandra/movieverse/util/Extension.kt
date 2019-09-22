package com.nandra.movieverse.util

import com.nandra.movieverse.network.Genre

fun List<Genre>.getStringGenre() : String {
    val stringBuilder = StringBuilder()
    return if (this.isEmpty()) {
        "No Genre Information"
    } else {
        this.forEach {
            stringBuilder.append(it.name)
            stringBuilder.append(", ")
        }
        val result = stringBuilder.delete(stringBuilder.length - 2, stringBuilder.length)
        result.toString()
    }
}