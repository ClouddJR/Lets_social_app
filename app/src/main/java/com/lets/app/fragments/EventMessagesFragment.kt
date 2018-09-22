package com.lets.app.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.repositories.MessagesRepository
import kotlinx.android.synthetic.main.fragment_event_messages.*

//kiedy potrzebujesz ID uzytkownika- UserRepository.getUserId()
//narazie na sztywno przypisz sobie id jakiegos wydarzenia

class EventMessagesFragment : BaseFragment() {

    val messagesRepository: MessagesRepository = MessagesRepository()
    val eventId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //w tym miejscu mozna uzywac UI
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