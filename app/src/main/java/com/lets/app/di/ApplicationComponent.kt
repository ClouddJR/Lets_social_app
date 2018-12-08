package com.lets.app.di

import com.lets.app.LetsApplication
import com.lets.app.activities.LoginActivity
import com.lets.app.activities.MainActivity
import com.lets.app.fragments.BaseFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(target: LetsApplication)
    fun inject(target: MainActivity)
    fun inject(target: LoginActivity)
    fun inject(target: BaseFragment)
}