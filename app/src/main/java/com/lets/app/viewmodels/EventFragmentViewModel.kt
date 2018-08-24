package com.lets.app.viewmodels

import android.content.Context
import android.location.Address
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.lets.app.repositories.GeocodingRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EventFragmentViewModel : ViewModel() {

    val addressOfLocation = MutableLiveData<List<Address>>()

    private val geocodingRepository = GeocodingRepository()
    private lateinit var disposable: Disposable

    fun init(location: GeoPoint, context: Context) {
        disposable = geocodingRepository.getAddressOfLocation(context, location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    addressOfLocation.value = it
                }
    }

    override fun onCleared() {
        super.onCleared()
        if(!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}