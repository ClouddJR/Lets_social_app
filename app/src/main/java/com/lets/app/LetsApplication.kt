package com.lets.app

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox

class LetsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initMapBox()
    }

    private fun initMapBox() {
        Mapbox.getInstance(applicationContext, getString(R.string.mapbox_access_token))
    }
}