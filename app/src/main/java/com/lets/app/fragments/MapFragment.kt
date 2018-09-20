package com.lets.app.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.adapters.RVBigEventMapAdapter
import com.lets.app.model.Event
import com.lets.app.viewmodels.EventsViewModel
import kotlinx.android.synthetic.main.fragment_map.*
import org.jetbrains.anko.toast

class MapFragment : BaseFragment() {

    private val eventsList = arrayListOf<Event>()
    private val markersList = arrayListOf<Marker>()

    private lateinit var viewModel: EventsViewModel
    private lateinit var googleMap: GoogleMap

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            googleMap = it
            getUserLocation()
            observeLocationUpdate()
            observeData()
            addMarkersToMap()
        }
        initFilterButton()
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

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(activity!!).get(EventsViewModel::class.java)
        viewModel.init()
    }

    private fun observeData() {
        viewModel.filteredEventsList.observe(this, Observer {
            it?.let {
                eventsList.removeAll(eventsList)
                eventsList.addAll(it)
                setRV(it)
            }
        })
    }

    private fun observeLocationUpdate() {
        viewModel.userLocation.observe(this, Observer {
            setInitialLocation(LatLng(it.latitude, it.longitude))
            initLocationButton()
        })

        viewModel.locationError.observe(this, Observer {
            displayWarningToast()
        })
    }

    private fun initFilterButton() {
        filtersButton.setOnClickListener {
            it.findNavController().navigate(R.id.filtersAction)
        }
    }

    private fun setRV(eventsList: List<Event>) {
        mapEventsRV.post {
            mapEventsRV.adapter = RVBigEventMapAdapter(eventsList, (mapEventsRV.width * 0.85).toInt())
        }
        attachSnapHelper()
    }

    private fun attachSnapHelper() {

        var previousPosition = -1
        var previousState = RecyclerView.SCROLL_STATE_DRAGGING
        val snapHelper = LinearSnapHelper()

        mapEventsRV.onFlingListener = null
        mapEventsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && previousState == RecyclerView.SCROLL_STATE_SETTLING) {

                    val view = snapHelper.findSnapView(recyclerView.layoutManager)
                    view?.let {
                        val position = recyclerView.layoutManager?.getPosition(view) ?: 0
                        if (position != previousPosition) {
                            val selectedEvent = eventsList[position]
                            zoomToLocation(LatLng(selectedEvent.location.latitude, selectedEvent.location.longitude))
                            changeMarkerColor(markersList[position])
                        }
                        previousPosition = position ?: -1
                    }

                }
                previousState = newState
            }
        })

        snapHelper.attachToRecyclerView(mapEventsRV)
    }

    private fun addMarkersToMap() {
        for (event in eventsList) {
            val marker = googleMap.addMarker(MarkerOptions()
                    .position(LatLng(event.location.latitude, event.location.longitude)))
            markersList.add(marker)
        }

        googleMap.setOnMarkerClickListener {
            mapEventsRV.post {
                mapEventsRV.smoothScrollToPosition(markersList.indexOf(it))
            }
            resetMarkerColors(it)
            changeMarkerColor(it)
            true
        }
    }

    private fun resetMarkerColors(previousMarker: Marker) {
        for (marker in markersList) {
            if (marker != previousMarker) {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker())
            }
        }
    }

    private fun getUserLocation() {
        viewModel.requestLocation(activity)
    }


    private fun setInitialLocation(lanLng: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(lanLng, 12f))
    }

    private fun zoomToLocation(lanLng: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory
                .newLatLng(lanLng))
    }

    private fun changeMarkerColor(marker: Marker) {
        resetMarkerColors(marker)
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
    }

    @SuppressLint("MissingPermission")
    private fun initLocationButton() {
        googleMap.isMyLocationEnabled = true
    }

    private fun displayWarningToast() {
        context?.toast("You need to accept location permission")
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

    override fun isUsingDataBinding() = false
}