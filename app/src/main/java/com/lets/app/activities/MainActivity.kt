package com.lets.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.lets.app.R
import com.lets.app.databinding.ActivityMainBinding
import com.lets.app.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

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
                navigateTo(R.id.homeFragment)
            }
        })

        viewModel.exploreButtonClick.observe(this, Observer {
            it.get()?.let {
                navigateTo(R.id.exploreFragment)
            }
        })

        viewModel.profileButtonClick.observe(this, Observer {
            it.get()?.let {
                navigateTo(R.id.profileFragment)
            }
        })

        viewModel.messagesButtonClick.observe(this, Observer {
            it.get()?.let {
                navigateTo(R.id.messagesFragment)
            }
        })
    }

    private fun navigateTo(resourceId: Int) {
        finalHost.navController.addOnNavigatedListener { _, destination ->
            when (destination.id) {
                R.id.homeFragment -> menuBottomNav.menu.findItem(R.id.homeButton).isChecked = true
                R.id.exploreFragment -> menuBottomNav.menu.findItem(R.id.exploreButton).isChecked = true
                R.id.profileFragment -> menuBottomNav.menu.findItem(R.id.profileButton).isChecked = true
                R.id.messagesFragment -> menuBottomNav.menu.findItem(R.id.messagesButton).isChecked = true
            }
        }

        if (!finalHost.navController.popBackStack(resourceId, false) && finalHost.navController.currentDestination.id != resourceId) {
            finalHost.navController.navigate(resourceId)
        }

    }


}
