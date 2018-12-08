package com.lets.app.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.model.Event
import com.lets.app.model.User
import com.lets.app.repositories.UserRepository
import com.lets.app.viewmodels.EventFragmentViewModel
import kotlinx.android.synthetic.main.fragment_event.*

class EventFragment : BaseFragment() {

    private lateinit var viewModel: EventFragmentViewModel
    private lateinit var googleMap: GoogleMap
    private lateinit var eventId: String
    private lateinit var eventOwnerId: String
    private lateinit var eventLocation: LatLng

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        receiveEventInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        initMapView()
        getEventFromPassedInfo()
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
        viewModel.clear()
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
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory)[EventFragmentViewModel::class.java]
    }

    private fun initMapView() {
        mapView.getMapAsync {
            googleMap = it
            it.uiSettings.setAllGesturesEnabled(false)
        }
        mapView.isClickable = true
    }

    private fun receiveEventInfo() {
        eventId = arguments?.getString("eventId") ?: ""
        eventOwnerId = arguments?.getString("eventOwner") ?: ""
        viewModel.init(eventId, eventOwnerId)
    }

    private fun getEventFromPassedInfo() {
        viewModel.event.observe(this, Observer {
            fillUiWithEventData(it)
        })

        viewModel.user.observe(this, Observer {
            fillUiWithUserData(it)
        })
    }

    private fun fillUiWithEventData(event: Event) {
        eventTitle.text = event.title
        eventDateTextView.text = event.timestamp.toDate().toString()
        eventAddressNameTextView.text = event.addressName
        aboutSectionTextView.text = event.description

        eventLocation = LatLng(event.location.latitude, event.location.longitude)

        if (::googleMap.isInitialized) {
            googleMap.addMarker(MarkerOptions().position(eventLocation))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 11.5f))
        }
    }

    private fun fillUiWithUserData(user: User) {
        hostNameTextView.text = user.fullName
        Glide.with(this)
                .load(UserRepository.buildCustomUserImageURL(user.id))
                .into(hostProfile)
    }

    override fun getLayoutId(): Int = R.layout.fragment_event

    override fun getToolbar(): Toolbar = toolbar

    override fun getToolbarTitle(): String {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return context?.getString(R.string.title_fragment_event) ?: ""
    }

    override fun shouldBottomNavBeVisible() = false

    override fun getFAB(): FloatingActionButton? = null

    override fun getFABAction(): () -> Unit = {}

    override fun isUsingDataBinding() = false
}