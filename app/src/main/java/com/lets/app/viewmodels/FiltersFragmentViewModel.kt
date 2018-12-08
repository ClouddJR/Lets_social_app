package com.lets.app.viewmodels

import android.content.Context
import android.view.View
import android.widget.Spinner
import androidx.lifecycle.MutableLiveData
import com.lets.app.databinding.FragmentFiltersBinding
import com.lets.app.utils.DateUtils.formatDate
import com.lets.app.utils.ExtFunctions.reset
import com.lets.app.utils.SingleEvent
import javax.inject.Inject

class FiltersFragmentViewModel @Inject constructor() : BaseViewModel() {

    val applyButtonClick = MutableLiveData<SingleEvent<Boolean>>()

    var dateSelected = false
        set(value) {
            field = value
            dateEditTextVisibility = when (value) {
                false -> View.GONE
                true -> View.VISIBLE
            }
            notifyChange()
        }

    var dateEditTextVisibility = View.GONE

    var filterYear = 0
    var filterMonth = 0
    var filterDay = 0

    fun setPreviousSelectionsIfNeeded(eventsViewModel: EventsViewModel, binding: FragmentFiltersBinding) {
        filterYear = eventsViewModel.filteredYear
        filterMonth = eventsViewModel.filteredMonth
        filterDay = eventsViewModel.filteredDay

        binding.categorySpinner.setSelection(eventsViewModel.categorySpinnerSelection)
        binding.sortingSpinner.setSelection(eventsViewModel.sortSpinnerSelection)

        if (filterYear != 0) {
            dateSelected = true
            binding.dateEditText.setText(formatDate(filterYear, filterMonth, filterDay))
        }
    }

    fun resetFilters(categorySpinner: Spinner, sortingSpinner: Spinner) {
        dateSelected = false
        filterYear = 0
        filterMonth = 0
        filterDay = 0
        categorySpinner.reset()
        sortingSpinner.reset()
        notifyChange()
    }

    fun filterAndSortEventsBasedOnSelection(categoryIndex: Int, sortingTypeIndex: Int, eventsViewModel: EventsViewModel, context: Context) {
        eventsViewModel.resetFilters()
        eventsViewModel.filterAndSort(filterYear, filterMonth, filterDay, categoryIndex, sortingTypeIndex)
        applyButtonClick.value = SingleEvent(true)
    }

}