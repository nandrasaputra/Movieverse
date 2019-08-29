package com.nandra.moviecatalogue.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FilmUsedTo (
    val title: String,
    val rating: String,
    val genre: String,
    val overview: String,
    val poster: Int
) : Parcelable