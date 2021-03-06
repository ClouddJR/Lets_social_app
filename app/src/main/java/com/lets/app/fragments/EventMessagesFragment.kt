package com.lets.app.fragments

import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.adapters.RVMessagesEventAdapter
import com.lets.app.model.MessagePack
import com.lets.app.viewmodels.MessagesViewModel
import kotlinx.android.synthetic.main.fragment_event_messages.*


class EventMessagesFragment : BaseFragment() {

    val eventId = "key1"

    private lateinit var viewModel: MessagesViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()


        sendButton.setOnClickListener {
            viewModel.addNewMessage(messageET.text.toString())
            messageET.setText("")
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory)[MessagesViewModel::class.java]
        viewModel.setChatId(eventId)
        viewModel.init()
    }

    private fun observeData() {
        viewModel.messagesList.observe(this, Observer {
            setRV(it)
        })
    }

    private fun setRV(list: List<MessagePack>) {
        var llm = LinearLayoutManager(this.context)
        messagesRV.layoutManager = llm
        messagesRV.adapter = RVMessagesEventAdapter(list)
        llm.scrollToPosition(list.size - 1)
    }


    override fun getLayoutId() = R.layout.fragment_event_messages

    override fun getToolbar(): Toolbar = toolbar

    override fun getToolbarTitle() = context?.getString(R.string.title_fragment_event_messages)
            ?: ""

    override fun shouldBottomNavBeVisible() = false

    override fun getFAB(): FloatingActionButton? = null

    override fun getFABAction() = {}

    override fun isUsingDataBinding() = false
}