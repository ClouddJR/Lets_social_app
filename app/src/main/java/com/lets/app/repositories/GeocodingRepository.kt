package com.lets.app.repositories

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.firebase.firestore.GeoPoint
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class GeocodingRepository @Inject constructor(private val context: Context) {

    fun getAddressOfLocation(location: GeoPoint): Observable<List<Address>> {
        val geocoder = Geocoder(context, Locale.getDefault())
        return Observable.fromCallable { geocoder.getFromLocation(location.latitude, location.longitude, 1) }
    }

}