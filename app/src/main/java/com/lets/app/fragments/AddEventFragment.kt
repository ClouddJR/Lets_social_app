package com.lets.app.fragments

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.utils.ExtFunctions.clearButton
import com.lets.app.utils.ExtFunctions.searchButton
import com.lets.app.utils.ExtFunctions.searchInput
import kotlinx.android.synthetic.main.fragment_add_event.*


class AddEventFragment : BaseFragment() {

    private lateinit var placesFragment: SupportPlaceAutocompleteFragment
    private lateinit var googleMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        setUpMapView()
        setUpAutoCompleteSearch()
        styleLocationEditText()

        createButton.setOnClickListener {
            val intentBuilder = PlacePicker.IntentBuilder()
            startActivityForResult(intentBuilder.build(activity), 123)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            showClearButtonOnLocationEditText()
        }
    }

    private fun setUpMapView() {
        mapView.getMapAsync {
            googleMap = it
        }
        mapView.isClickable = false
    }

    private fun setUpAutoCompleteSearch() {
        placesFragment = childFragmentManager.findFragmentById(R.id.place_autocomplete_fragment)
                as SupportPlaceAutocompleteFragment

        placesFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place?) {
                Log.d("placesSDK", place?.toString())
                place?.let {
                    mapView.visibility = View.VISIBLE
                    googleMap.addMarker(MarkerOptions().position(place.latLng))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 13f))
                    googleMap.uiSettings.setAllGesturesEnabled(false)
                }
            }

            override fun onError(status: Status?) {
                Log.d("placesSDK", status?.statusMessage)
            }
        })
    }

    private fun showClearButtonOnLocationEditText() {
        if (placesFragment.searchInput?.text?.isNotEmpty() == true) {
            placesFragment.clearButton?.visibility = View.VISIBLE
        }
    }

    private fun styleLocationEditText() {
        val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        placesFragment.searchButton?.layoutParams = layoutParams
        placesFragment.searchInput?.let {
            with(it) {
                hint = "Location"
                background = titleEditText.background.constantState.newDrawable().mutate()
                setHintTextColor(titleEditText.currentHintTextColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, titleEditText.textSize)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_add_event
    }

    override fun getToolbar(): Toolbar {
        return toolbar
    }

    override fun getToolbarTitle(): String {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return context?.getString(R.string.title_fragment_add_event) ?: ""
    }

    override fun shouldBottomNavBeVisible(): Boolean {
        return false
    }

    override fun getFAB(): FloatingActionButton? {
        return null
    }

    override fun getFABAction(): () -> Unit {
        return {}
    }
}