package com.nandra.movieverse.util

import android.view.View

fun View.setVisibilityVisible() {
    if (this.visibility != View.VISIBLE) {
        this.visibility = View.VISIBLE
    }
}

fun View.setVisibilityGone() {
    if (this.visibility != View.GONE) {
        this.visibility = View.GONE
    }
}