package com.lets.app.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.activities.LocationPickerActivity
import kotlinx.android.synthetic.main.fragment_add_event.*


class AddEventFragment : BaseFragment() {

    private val locationPickerRequest = 123

    private lateinit var googleMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        setUpMapView()
        setUpAutoCompleteSearch()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }


    private fun setUpMapView() {
        mapView.getMapAsync {
            googleMap = it
        }
        mapView.isClickable = false
    }

    private fun setUpAutoCompleteSearch() {
        locationEditText.setOnClickListener {
            startActivityForResult(Intent(context, LocationPickerActivity::class.java), locationPickerRequest)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == locationPickerRequest && resultCode == RESULT_OK) {
            data?.let {
                val lat = it.getDoubleExtra("lat", 0.0)
                val lng = it.getDoubleExtra("lng", 0.0)
                mapView.visibility = View.VISIBLE
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 16f))
                googleMap.uiSettings.setAllGesturesEnabled(false)
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