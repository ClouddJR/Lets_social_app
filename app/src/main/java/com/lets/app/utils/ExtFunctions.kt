package com.lets.app.utils

import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.lets.app.R

object ExtFunctions {

    val SupportPlaceAutocompleteFragment.searchButton
        get() = this.view?.findViewById<AppCompatImageButton>(R.id.place_autocomplete_search_button)

    fun View.resize(scale: Float) {
        this.animate().scaleXBy(scale).setDuration(100).start()
        this.animate().scaleYBy(scale).setDuration(100).start()
    }

    fun View.moveYBy(distance: Float) {
        this.animate().translationYBy(distance).setDuration(100).start()
    }

    fun ImageView.hasImageColor(color: Int): Boolean {
        return this.imageTintList.defaultColor == ContextCompat.getColor(context!!, color)
    }

    fun ImageView.setImageColor(color: Int) {
        this.imageTintList = ContextCompat.getColorStateList(context!!, color)
    }

    fun TextView.setColorText(color: Int) {
        return this.setTextColor(ContextCompat.getColor(context!!, color))
    }

    fun Spinner.reset() {
        this.setSelection(0, true)
    }

}