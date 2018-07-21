package com.lets.app.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.lets.app.R
import com.lets.app.databinding.ActivityMainBinding
import com.lets.app.model.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    val tag: String = MainActivity::class.java.simpleName

    private lateinit var finalHost: NavHostFragment
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDataBinding()
        initNavigation()
        observeBottomViewClicks()
    }

    private fun initDataBinding() {
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        binding.vm = viewModel
    }

    private fun initNavigation() {
        finalHost = NavHostFragment.create(R.navigation.nav_graph)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentPlaceHolder, finalHost)
                .setPrimaryNavigationFragment(finalHost)
                .commit()
    }

    private fun observeBottomViewClicks() {
        viewModel.homeButtonClick.observe(this, Observer {
            it.get()?.let {
                finalHost.navController.navigate(R.id.homeFragment)
            }
        })

        viewModel.mapButtonClick.observe(this, Observer {
            it.get()?.let {
                Log.d(tag, "MAP CLICK")
            }
        })

        viewModel.profileButtonClick.observe(this, Observer {
            it.get()?.let {
                Log.d(tag, "PROFILE CLICK")
            }
        })
    }


}
