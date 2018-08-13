package com.lets.app.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.activities.MainActivity
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

    override fun getFAB(): FloatingActionButton? {
        return FloatingActionButton(context).apply {
            setImageResource(R.drawable.ic_map_search_white_24dp)
            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, R.color.fabExploreFragmentColor))
        }
    }

    override fun getFABAction(): () -> Unit {
        return { (activity as? MainActivity)?.navController?.navigate(R.id.mapAction) }
    }
}