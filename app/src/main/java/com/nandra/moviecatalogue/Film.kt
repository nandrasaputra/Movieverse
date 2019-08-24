package com.nandra.moviecatalogue

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Film (
    val title: String,
    val rating: String,
    val genre: String,
    val overview: String,
    val poster: Int
) : Parcelable