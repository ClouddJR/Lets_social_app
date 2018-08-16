package com.lets.app.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.adapters.RVBigEventAdapter
import com.lets.app.model.Event
import com.lets.app.viewmodels.EventsViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {

    private lateinit var viewModel: EventsViewModel

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
            setRV(it)
        })
    }

    private fun setRV(list: List<Event>) {
        var previousState = RecyclerView.SCROLL_STATE_DRAGGING
        val snapHelper = LinearSnapHelper()

        organizedRV.adapter = RVBigEventAdapter(list)
        organizedRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && previousState == RecyclerView.SCROLL_STATE_SETTLING) {

                    val view = snapHelper.findSnapView(recyclerView.layoutManager)
                    view?.let {
                        val position = recyclerView.layoutManager?.getPosition(view)
                        Log.d("SNAPposition", position.toString())
                    }

                }
                previousState = newState
            }
        })
        snapHelper.attachToRecyclerView(organizedRV)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun getToolbar(): Toolbar {
        return toolbar
    }

    override fun getToolbarTitle(): String {
        return context?.getString(R.string.title_fragment_profile) ?: ""
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