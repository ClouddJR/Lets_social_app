package com.lets.app.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearSnapHelper
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.adapters.RVBigEventAdapter
import com.lets.app.model.Event
import com.lets.app.repositories.UserRepository
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProfilePicture()
    }

    private fun loadProfilePicture() {
        Glide.with(this)
                .load(UserRepository.buildUserImageURL())
                .into(profileImage)
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
        organizedRV.adapter = RVBigEventAdapter(list)
        attachSnapHelper()
    }

    private fun attachSnapHelper() {
        organizedRV.onFlingListener = null
        LinearSnapHelper().attachToRecyclerView(organizedRV)
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

    override fun isUsingDataBinding() = false
}