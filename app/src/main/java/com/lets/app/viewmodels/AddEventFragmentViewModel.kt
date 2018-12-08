package com.lets.app.viewmodels

import android.location.Address
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.lets.app.EventsRepository
import com.lets.app.R
import com.lets.app.model.Event
import com.lets.app.repositories.GeocodingRepository
import com.lets.app.repositories.UserRepository
import com.lets.app.utils.CategoriesUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class AddEventFragmentViewModel @Inject constructor(private val userRepository: UserRepository,
                                                    private val eventsRepository: EventsRepository,
                                                    private val geocodingRepository: GeocodingRepository) : ViewModel() {

    var eventTitle = ""
    var eventDesc = ""
    var addressName = ""
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
    private lateinit var userDisposable: Disposable

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

    fun getReadableInfoAboutLocation() {
        geocodingDisposable = geocodingRepository.getAddressOfLocation(
                GeoPoint(chosenEventLocation.value?.latitude ?: 0.0,
                        chosenEventLocation.value?.longitude ?: 0.0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    addressOfChosenLocation.value = it
                }
    }

    fun addNewEvent() {

        val ownerId = UserRepository.getUserId()
        val calendar = Calendar.getInstance().also { it.set(year, month - 1, day, hour, minute, 0) }
        val eventTimestamp = Timestamp(calendar.time)
        val location = GeoPoint(chosenEventLocation.value?.latitude ?: 0.0,
                chosenEventLocation.value?.longitude ?: 0.0)

        userDisposable = userRepository.getUserFromId(ownerId)
                .subscribe {
                    val eventToBeAdded = Event("", ownerId, it.fullName, eventTitle, eventDesc, eventTimestamp, location,
                            addressName, isPublic, maxPeople, ageFrom, ageTo, selectedSex, type, category, false)

                    eventsRepository.addEvent(eventToBeAdded)
                }

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

        if (::userDisposable.isInitialized && !userDisposable.isDisposed) {
            userDisposable.dispose()
        }
    }
}