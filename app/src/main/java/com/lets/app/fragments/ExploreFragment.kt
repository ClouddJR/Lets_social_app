package com.lets.app.fragments

import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lets.app.R
import com.lets.app.adapters.RVBigEventAdapter
import com.lets.app.model.Event
import com.lets.app.viewmodels.EventsViewModel
import kotlinx.android.synthetic.main.fragment_explore.*

class ExploreFragment : BaseFragment() {

    private lateinit var viewModel: EventsViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    override fun onResume() {
        super.onResume()
        showFAB()
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

    private fun showFAB() {
        switchFAB.show()
    }

    private fun setRV(list: List<Event>) {
        nearbyEventsRV.adapter = RVBigEventAdapter(list)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_explore
    }

    override fun getToolbar(): Toolbar {
        return toolbar
    }

    override fun getToolbarTitle(): String {
        return context?.getString(R.string.title_fragment_explore) ?: ""
    }

    override fun shouldBottomNavBeVisible(): Boolean {
        return true
    }
}