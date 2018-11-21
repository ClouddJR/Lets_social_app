package com.lets.app.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.EventsRepository
import com.lets.app.R
import com.lets.app.adapters.RVMessagesAdapter
import com.lets.app.model.Event
import com.lets.app.repositories.UserRepository
import com.lets.app.viewmodels.EventsViewModel
import kotlinx.android.synthetic.main.fragment_messages.*

class MessagesFragment : BaseFragment() {

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

        EventsRepository().getEvents(UserRepository.getUserId()).
    }

    private fun setRV(list: List<Event>) {
        chatsRV.adapter = RVMessagesAdapter(list)
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_messages
    }

    override fun getToolbar(): Toolbar {
        return toolbar
    }

    override fun getToolbarTitle(): String {
        return context?.getString(R.string.title_fragment_messages) ?: ""
    }

    override fun shouldBottomNavBeVisible(): Boolean {
        return true
    }

    override fun getFAB(): FloatingActionButton? {
        return FloatingActionButton(context).apply {
            setImageResource(R.drawable.ic_map_search_white_24dp)
            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, R.color.colorAccent))
        }
    }

    override fun getFABAction(): () -> Unit {
        return {}
    }

    override fun isUsingDataBinding() = false
}