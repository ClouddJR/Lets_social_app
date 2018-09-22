package com.lets.app.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import kotlinx.android.synthetic.main.fragment_messages.*

class MessagesFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messagesButton.setOnClickListener {
            it.findNavController().navigate(R.id.eventMessagesAction)
        }
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