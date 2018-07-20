package com.lets.app.fragments

import androidx.appcompat.widget.Toolbar
import com.lets.app.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun getToolbar(): Toolbar {
        return toolbar
    }
}