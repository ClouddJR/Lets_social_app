package com.lets.app.fragments

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import kotlinx.android.synthetic.main.fragment_event.*

class EventFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_event

    override fun getToolbar(): Toolbar = toolbar

    override fun getToolbarTitle(): String {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return context?.getString(R.string.title_fragment_event) ?: ""
    }

    override fun shouldBottomNavBeVisible() = false

    override fun getFAB(): FloatingActionButton? = null

    override fun getFABAction(): () -> Unit = {}
}