package com.lets.app.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.GeoPoint
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        receiveEventInfo()
        getEventFromPassedInfo()
        initMapView()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(activity!!).get(EventFragmentViewModel::class.java)
    }

    private fun receiveEventInfo() {
        eventId = arguments?.getString("eventId") ?: ""
        val eventLat = arguments?.getString("eventLat")?.toDouble() ?: 0.0
        val eventLon = arguments?.getString("eventLon")?.toDouble() ?: 0.0

        val eventLocation = GeoPoint(eventLat, eventLon)

        viewModel.init(eventId, eventLocation)
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

        Log.d("joinedUsers", event.joined.toString())
    }

    private fun fillUiWithUserData(user: User) {
        hostNameTextView.text = user.fullName
        Glide.with(this)
                .load(UserRepository.buildUserImageURL())
                .into(hostProfile)
    }

    private fun initMapView() {
        mapView.getMapAsync {
            it.addMarker(MarkerOptions().position(LatLng(52.2919238, 16.7315878)))
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(52.2919238, 16.7315878), 10.5f))
            it.uiSettings.setAllGesturesEnabled(false)
        }
        mapView.isClickable = true
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