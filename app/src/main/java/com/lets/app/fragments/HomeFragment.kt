package com.lets.app.fragments

import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.adapters.RVBigEventAdapter
import com.lets.app.adapters.RVSmallEventAdapter
import com.lets.app.model.Event
import com.lets.app.utils.StartLinearSnapHelper
import com.lets.app.viewmodels.EventsViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private lateinit var viewModel: EventsViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
        plusIcon.setOnClickListener {
            it.findNavController().navigate(R.id.addEventAction)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(activity!!).get(EventsViewModel::class.java)
        viewModel.init()
    }

    private fun observeData() {
        viewModel.nearbyEventsList.observe(this, Observer {
            setRV(it)
        })
    }

    private fun setRV(list: List<Event>) {
        yourEventsRV.adapter = RVSmallEventAdapter(list)
        joinedEventsRV.adapter = RVSmallEventAdapter(list)
        nearbyEventsRV.adapter = RVBigEventAdapter(list)
        attachSnapHelper()
    }

    private fun attachSnapHelper() {
        yourEventsRV.onFlingListener = null
        joinedEventsRV.onFlingListener = null
        StartLinearSnapHelper().attachToRecyclerView(yourEventsRV)
        StartLinearSnapHelper().attachToRecyclerView(joinedEventsRV)
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun getToolbar(): Toolbar {
        return toolbar
    }

    override fun getToolbarTitle(): String {
        return context?.getString(R.string.title_fragment_home) ?: ""
    }

    override fun shouldBottomNavBeVisible(): Boolean {
        return true
    }

    override fun getFAB(): FloatingActionButton? {
        return null
    }

    override fun getFABAction(): () -> Unit {
        return {}
    }
}