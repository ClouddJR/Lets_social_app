package com.lets.app

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.lets.app.di.ApplicationComponent
import com.lets.app.di.ApplicationModule
import com.lets.app.di.DaggerApplicationComponent
import com.squareup.leakcanary.LeakCanary

class LetsApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
        initFirestoreSettings()
    }

    private fun initFirestoreSettings() {
        val firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()
        firestore.firestoreSettings = settings

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }


}