package com.lets.app.viewmodels

import android.content.Context
import android.location.Address
import android.util.Log
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.lets.app.EventsRepository
import com.lets.app.R
import com.lets.app.model.Event
import com.lets.app.repositories.GeocodingRepository
import com.lets.app.utils.CategoriesUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class AddEventFragmentViewModel : ViewModel() {

    private val eventsRepository = EventsRepository()
    private val geocodingRepository = GeocodingRepository()

    var eventTitle = ""
    var eventDesc = ""
    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0

    var isPublic = true

    var maxPeople = 0
    var ageFrom = 0
    var ageTo = 0

    var isMaxPeopleSpecified = false
    var isAgeRangeSpecified = false

    var selectedSex = 0b11111
    var type = 20
    var category = 0

    val chosenEventLocation = MutableLiveData<LatLng>()
    val addressOfChosenLocation = MutableLiveData<List<Address>>()

    val maxPeopleCheckboxClick = MutableLiveData<Boolean>()
    val ageCheckboxClick = MutableLiveData<Boolean>()

    private lateinit var geocodingDisposable: Disposable

    fun setEventCategory(view: ImageView) {
        val eventCategory = CategoriesUtils.getEventCategoryFromImage(view.id)
        category = eventCategory.id
    }

    fun setEventSex(index: Int) {
        when (index) {
            R.id.femaleRadioButton -> selectedSex = 0x1
            R.id.maleRadioButton -> selectedSex = 0x2
            R.id.allRadioButton -> selectedSex = 0b11111
        }
    }

    fun setEventLocation(chosenLocation: LatLng) {
        chosenEventLocation.value = chosenLocation
    }

    fun getReadableInfoAboutLocation(context: Context) {
        geocodingDisposable = geocodingRepository.getAddressOfLocation(context,
                GeoPoint(chosenEventLocation.value?.latitude ?: 0.0,
                        chosenEventLocation.value?.longitude ?: 0.0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    addressOfChosenLocation.value = it
                }
    }

    fun addNewEvent() {
        val ownerId = AccessToken.getCurrentAccessToken().userId
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day, hour, minute)
        val eventTimestamp = Timestamp(calendar.time)
        val location = GeoPoint(chosenEventLocation.value?.latitude ?: 0.0,
                chosenEventLocation.value?.longitude ?: 0.0)

        val eventToBeAdded = Event("", ownerId, eventTitle, eventDesc, eventTimestamp, location
                , isPublic, maxPeople, ageFrom, ageTo, selectedSex, type, category, false)

        eventsRepository.addEvent(eventToBeAdded)

        Log.d("AddEventViewModel", eventToBeAdded.toString())
    }

    fun onMaxPeopleCheckBoxClicked(buttonView: CompoundButton, isChecked: Boolean) {
        isMaxPeopleSpecified = !isChecked
        maxPeopleCheckboxClick.value = isChecked
    }

    fun onAgeCheckBoxClicked(buttonView: CompoundButton, isChecked: Boolean) {
        isAgeRangeSpecified = !isChecked
        ageCheckboxClick.value = isChecked
    }

    override fun onCleared() {
        super.onCleared()
        if (::geocodingDisposable.isInitialized && !geocodingDisposable.isDisposed) {
            geocodingDisposable.dispose()
        }
    }
}