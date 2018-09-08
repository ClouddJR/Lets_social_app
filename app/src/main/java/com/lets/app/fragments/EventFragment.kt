package com.lets.app.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.GeoPoint
import com.lets.app.R
import com.lets.app.viewmodels.EventFragmentViewModel
import kotlinx.android.synthetic.main.fragment_event.*

class EventFragment : BaseFragment() {

    private lateinit var viewModel: EventFragmentViewModel
    private lateinit var googleMap: GoogleMap

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeGeocoding()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        initMapView()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(activity!!).get(EventFragmentViewModel::class.java)
        viewModel.init(GeoPoint(52.2919238, 16.7315878), context!!)
    }

    private fun observeGeocoding() {
        viewModel.addressOfLocation.observe(this, Observer {
            for (address in it) {
                Log.d("locationInfo", "${address.featureName}, ${address.locality}")
            }
        })
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