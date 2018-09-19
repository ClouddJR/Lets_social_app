package com.lets.app.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import androidx.fragment.app.FragmentActivity
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
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class EventsViewModel : ViewModel() {

    private lateinit var eventsDisposable: Disposable
    private lateinit var permissionDisposable: Disposable

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val userLocation = MutableLiveData<Location>()
    val locationError = MutableLiveData<Boolean>()

    val nearbyEventsList = MutableLiveData<List<Event>>()

    val filteredEventsList = MutableLiveData<List<Event>>()

    var categorySpinnerSelection = 0
    var sortSpinnerSelection = 0

    var filteredYear = 0
    var filteredMonth = 0
    var filteredDay = 0


    fun init() {
        if (nearbyEventsList.value?.isEmpty() != false) {
            eventsDisposable = EventsRepository().getEventsNearby()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        nearbyEventsList.value = it
                        filteredEventsList.value = nearbyEventsList.value?.toMutableList()
                        filterAndSort()
                    }
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
        }

    }

    private fun filterByCategory(categoryIndex: Int) {
        categorySpinnerSelection = categoryIndex
        val category = CategoriesUtils.getEventCategoryFromSpinner(categoryIndex)

        if (category != EventCategory.ALL) {
            filteredEventsList.value = filteredEventsList.value?.filter {
                it.category == category.id
            }
        }
    }

    private fun sortEvents(sortingSpinnerIndex: Int) {
        sortSpinnerSelection = sortingSpinnerIndex

        val sortType = getSortTypeFromSpinner(sortingSpinnerIndex)
        val eventsList = filteredEventsList.value!!

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

    fun requestLocation(activity: Activity?) {
        permissionDisposable = RxPermissions(activity as FragmentActivity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { granted ->
                    if (granted) {
                        getUserLocation(activity)
                    } else {
                        errorWhileGettingLocation()
                    }
                }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation(activity: Activity?) {
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

    private fun errorWhileGettingLocation() {
        locationError.value = true
    }

    override fun onCleared() {
        super.onCleared()
        if (::eventsDisposable.isInitialized && !eventsDisposable.isDisposed) {
            eventsDisposable.dispose()
        }

        if (::permissionDisposable.isInitialized && !permissionDisposable.isDisposed) {
            permissionDisposable.dispose()
        }
    }
}