package com.lets.app.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.model.Event
import com.lets.app.viewmodels.EventsViewModel
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : BaseFragment() {

    private val eventsList = arrayListOf<Event>()

    private lateinit var viewModel: EventsViewModel
    private lateinit var mapBoxMap: MapboxMap

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(activity!!).get(EventsViewModel::class.java)
        viewModel.init()
    }

    private fun observeData() {
        viewModel.nearbyEventsList.observe(this, Observer {
            eventsList.addAll(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            mapBoxMap = it
            addMarkersToMap()
            mapView.findViewById<ImageView>(R.id.logoView).imageAlpha = 0

        }
    }

    private fun addMarkersToMap() {
        for (event in eventsList) {
            mapBoxMap.addMarker(MarkerOptions()
                    .position(LatLng(event.location.latitude, event.location.longitude)))
                    .title = event.title
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_map
    }

    override fun getToolbar(): Toolbar {
        return toolbar
    }

    override fun getToolbarTitle(): String {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return context?.getString(R.string.title_fragment_map) ?: ""
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