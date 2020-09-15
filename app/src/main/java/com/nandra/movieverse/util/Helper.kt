package com.nandra.movieverse.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import com.endiar.movieverse.core.utils.Constant
import com.nandra.movieverse.R
import java.text.SimpleDateFormat

sealed class FilmType(val typeValue: String) {
    object FilmTypeMovie : FilmType("movie")
    object FilmTypeTV : FilmType("tv")
}

@SuppressLint("SimpleDateFormat")
fun formatDate(stringDate: String) : String {
    val inFormat = SimpleDateFormat("yyyy-MM-dd")
    val outFormat = SimpleDateFormat("dd-MM-yyyy")

    return try {
        val date = inFormat.parse(stringDate)
        outFormat.format(date ?: "")
    } catch (exp: Exception) {
        ""
    }
}

fun tabTitleProvider(context: Context, position: Int): String {
    return when(position) {
        0 -> context.getString(R.string.discover_tab_title_position_0)
        1 -> context.getString(R.string.discover_tab_title_position_1)
        else -> throw Exception("Invalid Position")
    }
}

fun getFilmTypeFromString(typeInString: String): FilmType {
    return when(typeInString) {
        Constant.MOVIE_FILM_TYPE -> {
            FilmType.FilmTypeMovie
        }
        Constant.TV_FILM_TYPE -> {
            FilmType.FilmTypeTV
        }
        else -> throw Exception("Invalid typeInString")
    }
}

fun hideKeyboardFrom(context: Context, view: View) {
    val imm: InputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showKeyboardFrom(context: Context) {
    val imm: InputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun changeThemeByThemeValue(value: String) {
    when(value) {
        "dark" -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)}
        "day" -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)}
        else -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)}
    }
}