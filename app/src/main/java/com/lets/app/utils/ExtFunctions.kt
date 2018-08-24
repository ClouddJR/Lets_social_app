package com.lets.app.utils

import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageButton
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.lets.app.R

object ExtFunctions {

    val SupportPlaceAutocompleteFragment.clearButton
        get() = this.view?.findViewById<AppCompatImageButton>(R.id.place_autocomplete_clear_button)

    val SupportPlaceAutocompleteFragment.searchInput
        get() = this.view?.findViewById<EditText>(R.id.place_autocomplete_search_input)

    val SupportPlaceAutocompleteFragment.searchButton
        get() = this.view?.findViewById<AppCompatImageButton>(R.id.place_autocomplete_search_button)

}