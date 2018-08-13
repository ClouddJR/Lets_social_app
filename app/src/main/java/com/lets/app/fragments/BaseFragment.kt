package com.lets.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.activities.MainActivity


abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        connectMethodsWithMainActivity()
    }

    private fun connectMethodsWithMainActivity() {
        (activity as? AppCompatActivity)?.setSupportActionBar(getToolbar())
        (activity as? AppCompatActivity)?.supportActionBar?.title = getToolbarTitle()
        (activity as? MainActivity)?.toggleBottomNavBar(shouldBottomNavBeVisible())
        (activity as? MainActivity)?.setUpFAB(getFAB(), getFABAction())
    }

    abstract fun getLayoutId(): Int

    abstract fun getToolbar(): Toolbar

    abstract fun getToolbarTitle(): String

    abstract fun shouldBottomNavBeVisible(): Boolean

    abstract fun getFAB(): FloatingActionButton?

    abstract fun getFABAction(): () -> Unit

}