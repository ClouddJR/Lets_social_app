package com.lets.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.facebook.FacebookSdk
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.databinding.ActivityMainBinding
import com.lets.app.repositories.UserRepository
import com.lets.app.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val tag: String = MainActivity::class.java.simpleName

    lateinit var navController: NavController
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FacebookSdk()", FacebookSdk.getApplicationSignature(this))
        navigateToLogin()
        //initLocation()
        initDataBinding()
        initNavController()
        observeBottomViewClicks()
    }

    override fun onSupportNavigateUp() = findNavController(R.id.fragmentPlaceHolder).navigateUp()


    private fun navigateToLogin() {
        if (!UserRepository.isUserLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

//    private fun initLocation() {
//        ViewModelProviders.of(this).get(EventsViewModel::class.java).requestLocation(this)
//    }

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

        if (!navController.popBackStack(resourceId, false) && navController.currentDestination?.id != resourceId) {
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

    fun toggleBottomNavBar(shouldBeVisible: Boolean) {
        menuBottomNav.visibility = if (shouldBeVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun setUpFAB(passedFAB: FloatingActionButton?, function: () -> Unit) {
        fab.hide()
        fab.setOnClickListener(null)
        passedFAB?.let {
            fab.backgroundTintList = passedFAB.backgroundTintList
            fab.setImageDrawable(passedFAB.drawable)
            fab.show()
            fab.setOnClickListener {
                function.invoke()
            }

        }
    }

}
