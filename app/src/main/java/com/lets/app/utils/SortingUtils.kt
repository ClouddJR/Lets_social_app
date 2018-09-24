package com.lets.app.utils

import android.location.Location
import com.lets.app.model.Event

object SortingUtils {

    enum class SortType {
        DATE_ASCENDING,
        DATE_DESCENDING,
        CLOSEST_TO_FARTHEST,
        FARTHEST_TO_CLOSEST
    }

    fun getSortTypeFromSpinner(index: Int): SortType {
        return when (index) {
            0 -> SortType.DATE_DESCENDING
            1 -> SortType.DATE_ASCENDING
            2 -> SortType.CLOSEST_TO_FARTHEST
            3 -> SortType.FARTHEST_TO_CLOSEST
            else -> SortType.DATE_DESCENDING //default (from newest to oldest)
        }
    }

    fun sortByDateAscending(list: MutableList<Event>): MutableList<Event> {
        return list.sortedWith(dateAscendingSort).toMutableList()
    }

    fun sortByDateDescending(list: MutableList<Event>): MutableList<Event> {
        return list.sortedWith(dateDescendingSort).toMutableList()
    }

    private val dateAscendingSort = Comparator<Event> { event1, event2 ->
        when {
            event2.timestamp.seconds == event1.timestamp.seconds -> 0
            event2.timestamp.seconds > event1.timestamp.seconds -> 1
            else -> -1
        }
    }

    private val dateDescendingSort = Comparator<Event> { event1, event2 ->
        when {
            event1.timestamp.seconds == event2.timestamp.seconds -> 0
            event1.timestamp.seconds > event2.timestamp.seconds -> 1
            else -> -1
        }
    }

    fun sortClosestToFarthest(list: MutableList<Event>, userLocation: Location): MutableList<Event> {

        return list.sortedWith(Comparator { event1, event2 ->
            val event1Location = Location("")
            event1Location.latitude = event1.location.latitude
            event1Location.longitude = event1.location.longitude

            val event2Location = Location("")
            event2Location.latitude = event2.location.latitude
            event2Location.longitude = event2.location.longitude

            when {
                userLocation.distanceTo(event1Location) == userLocation.distanceTo(event2Location) -> 0
                userLocation.distanceTo(event1Location) > userLocation.distanceTo(event2Location) -> 1
                else -> -1
            }
        }).toMutableList()
    }


    fun sortFarthestToClosest(list: List<Event>, userLocation: Location): MutableList<Event> {

        return list.sortedWith(Comparator { event1, event2 ->
            val event1Location = Location("")
            event1Location.latitude = event1.location.latitude
            event1Location.longitude = event1.location.longitude

            val event2Location = Location("")
            event2Location.latitude = event2.location.latitude
            event2Location.longitude = event2.location.longitude

            when {
                userLocation.distanceTo(event1Location) == userLocation.distanceTo(event2Location) -> 0
                userLocation.distanceTo(event1Location) > userLocation.distanceTo(event2Location) -> -1
                else -> 1
            }
        }).toMutableList()
    }
}