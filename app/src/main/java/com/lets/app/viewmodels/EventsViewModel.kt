package com.lets.app.viewmodels

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lets.app.EventsRepository
import com.lets.app.model.Event
import com.lets.app.utils.CategoriesUtils
import com.lets.app.utils.CategoriesUtils.EventCategory
import com.lets.app.utils.SortingUtils.SortType
import com.lets.app.utils.SortingUtils.getSortTypeFromSpinner
import com.lets.app.utils.SortingUtils.sortByDateAscending
import com.lets.app.utils.SortingUtils.sortByDateDescending
import com.lets.app.utils.SortingUtils.sortClosestToFarthest
import com.lets.app.utils.SortingUtils.sortFarthestToClosest
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

class EventsViewModel @Inject constructor(private val eventsRepository: EventsRepository) : ViewModel() {

    private lateinit var eventsDisposable: Disposable

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val userLocation = MutableLiveData<Location>()
    val locationError = MutableLiveData<Boolean>()

    private val eventsList = mutableListOf<Event>()
    val nearbyEventsList = MutableLiveData<MutableList<Event>>()

    val filteredEventsList = MutableLiveData<MutableList<Event>>()

    var categorySpinnerSelection = 0
    var sortSpinnerSelection = 0

    var filteredYear = 0
    var filteredMonth = 0
    var filteredDay = 0


    fun init() {
        getEventsNearby()
    }

    private fun getEventsNearby() {
        if (eventsList.isEmpty()) {
            Observable
                    .merge(eventsRepository.getEventsNearby())
                    .subscribe(object : Observer<Event> {
                        override fun onNext(passedEvent: Event) {
                            if (passedEvent !in eventsList) {
                                eventsList.add(0, passedEvent)
                            } else {
                                eventsList[eventsList.indexOf(passedEvent)] = passedEvent.copy()
                            }

                            nearbyEventsList.value = eventsList
                            filteredEventsList.value = eventsList
                            filterAndSort()
                        }

                        override fun onError(e: Throwable) {
                            //not used
                        }

                        override fun onComplete() {
                            //not used
                        }

                        override fun onSubscribe(d: Disposable) {
                            eventsDisposable = d
                        }
                    })
        }
    }

    fun resetFilters() {
        filteredEventsList.value = nearbyEventsList.value?.toMutableList()
    }

    fun filterAndSort(year: Int = filteredYear, month: Int = filteredMonth, day: Int = filteredDay,
                      categoryIndex: Int = categorySpinnerSelection,
                      sortingSpinnerIndex: Int = sortSpinnerSelection) {
        if (year != 0) filterByDate(year, month, day) else resetDateFilter()
        filterByCategory(categoryIndex)
        sortEvents(sortingSpinnerIndex)
    }

    private fun resetDateFilter() {
        filteredYear = 0
        filteredMonth = 0
        filteredDay = 0
    }

    private fun filterByDate(year: Int, month: Int, day: Int) {
        filteredYear = year
        filteredMonth = month
        filteredDay = day

        filteredEventsList.value = nearbyEventsList.value?.filter {
            val eventCalendar = Calendar.getInstance()
            eventCalendar.time = it.timestamp.toDate()
            eventCalendar.get(Calendar.YEAR) == year
                    && eventCalendar.get(Calendar.MONTH) == month - 1
                    && eventCalendar.get(Calendar.DAY_OF_MONTH) == day
        }?.toMutableList()

    }

    private fun filterByCategory(categoryIndex: Int) {
        categorySpinnerSelection = categoryIndex
        val category = CategoriesUtils.getEventCategoryFromSpinner(categoryIndex)

        if (category != EventCategory.ALL) {
            filteredEventsList.value = filteredEventsList.value?.filter {
                it.category == category.id
            }?.toMutableList()
        }
    }

    private fun sortEvents(sortingSpinnerIndex: Int) {
        sortSpinnerSelection = sortingSpinnerIndex

        val sortType = getSortTypeFromSpinner(sortingSpinnerIndex)
        val eventsList = filteredEventsList.value

        eventsList?.let {
            filteredEventsList.value = when (sortType) {
                SortType.DATE_DESCENDING -> {
                    sortByDateDescending(eventsList)
                }
                SortType.DATE_ASCENDING -> {
                    sortByDateAscending(eventsList)
                }
                SortType.FARTHEST_TO_CLOSEST -> {
                    sortFarthestToClosest(eventsList, userLocation.value!!)
                }
                SortType.CLOSEST_TO_FARTHEST -> {
                    sortClosestToFarthest(eventsList, userLocation.value!!)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation(activity: Activity?) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { location ->
                    setUserLocation(location)
                }
            }
        }
    }

    private fun setUserLocation(location: Location) {
        userLocation.value = location
    }

    fun errorWhileGettingLocation() {
        locationError.value = true
    }

    override fun onCleared() {
        super.onCleared()
        if (::eventsDisposable.isInitialized && !eventsDisposable.isDisposed) {
            eventsDisposable.dispose()
        }
    }
}