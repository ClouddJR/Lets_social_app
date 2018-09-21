package com.lets.app

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class LetsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initFirestoreSettings()
    }

    private fun initFirestoreSettings() {
        val firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()
        firestore.firestoreSettings = settings
    }
}