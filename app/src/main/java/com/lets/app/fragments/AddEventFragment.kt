package com.lets.app.fragments

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.lets.app.R
import kotlinx.android.synthetic.main.fragment_add_event.*

class AddEventFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_add_event
    }

    override fun getToolbar(): Toolbar {
        return toolbar
    }

    override fun getToolbarTitle(): String {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return context?.getString(R.string.title_fragment_add_event) ?: ""
    }

    override fun shouldBottomNavBeVisible(): Boolean {
        return false
    }
}