package com.lets.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.lets.app.R
import com.lets.app.databinding.ActivityMainBinding
import com.lets.app.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val tag: String = MainActivity::class.java.simpleName

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToLogin()
        initDataBinding()
        initNavController()
        observeBottomViewClicks()
    }

    override fun onSupportNavigateUp() = findNavController(R.id.fragmentPlaceHolder).navigateUp()


    private fun navigateToLogin() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            Log.d("ArekAuth","navigating")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun initDataBinding() {
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        binding.vm = viewModel
    }

    private fun initNavController() {
        navController = Navigation.findNavController(this, R.id.fragmentPlaceHolder)

        navController.addOnNavigatedListener { _, destination ->
            when (destination.id) {
                R.id.homeFragment -> menuBottomNav.menu.findItem(R.id.homeButton).isChecked = true
                R.id.exploreFragment -> menuBottomNav.menu.findItem(R.id.exploreButton).isChecked = true
                R.id.profileFragment -> menuBottomNav.menu.findItem(R.id.profileButton).isChecked = true
                R.id.messagesFragment -> menuBottomNav.menu.findItem(R.id.messagesButton).isChecked = true
            }
        }
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

        if (!navController.popBackStack(resourceId, false) && navController.currentDestination.id != resourceId) {
            navController.navigate(getActionId(resourceId))
        }

    }

    private fun getActionId(resourceId: Int): Int {
        return when (resourceId) {
            R.id.homeFragment -> R.id.homeAction
            R.id.exploreFragment -> R.id.exploreAction
            R.id.profileFragment -> R.id.profileAction
            R.id.messagesFragment -> R.id.messagesAction
            else -> 0
        }
    }

}
