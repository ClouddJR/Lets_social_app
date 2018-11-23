package com.lets.app.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.lets.app.R
import kotlinx.android.synthetic.main.activity_location_picker.*

class LocationPickerActivity : AppCompatActivity() {

    private val placesAPIRequest = 10
    private var restoredLocation: LatLng? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_picker)
        mapView.onCreate(savedInstanceState)
        setUpToolbar()
        setUpMap()
        setUpPlacesEditText()
        setUpLocationPickedButton()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        googleMap.isMyLocationEnabled = false
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putDouble("lat", googleMap.cameraPosition.target.latitude)
        outState?.putDouble("lng", googleMap.cameraPosition.target.longitude)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            restoredLocation = LatLng(
                    it.getDouble("lat"),
                    it.getDouble("lng")
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == placesAPIRequest && resultCode == Activity.RESULT_OK) {
            val place = PlaceAutocomplete.getPlace(this, data)
            animateToLocation(place.latLng)
            placesEditText.setText(place.name)
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUpMap() {
        mapView.getMapAsync {
            googleMap = it
            if (restoredLocation == null) {
                getUserLocation()
            } else {
                restorePositionAfterRotation()
            }
        }
    }

    private fun restorePositionAfterRotation() {
        placeMapCenterInLocation(restoredLocation!!)
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { location ->
                    placeMapCenterInLocation(LatLng(location.latitude, location.longitude))
                }
            }
        }
    }

    private fun animateToLocation(lanLng: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(lanLng, 16f))
    }

    private fun placeMapCenterInLocation(latLng: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(latLng, 16f))
    }

    private fun setUpPlacesEditText() {
        placesEditText.setOnClickListener {
            startPlacesAutocompleteActivity()
        }

        searchButton.setOnClickListener {
            startPlacesAutocompleteActivity()
        }
    }

    private fun startPlacesAutocompleteActivity() {
        val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this)
        startActivityForResult(intent, placesAPIRequest)
    }

    private fun setUpLocationPickedButton() {
        pickButton.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("lat", googleMap.cameraPosition.target.latitude)
            resultIntent.putExtra("lng", googleMap.cameraPosition.target.longitude)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
