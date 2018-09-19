package com.lets.app.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.databinding.FragmentFiltersBinding
import com.lets.app.utils.DateUtils
import com.lets.app.viewmodels.EventsViewModel
import com.lets.app.viewmodels.FiltersFragmentViewModel
import kotlinx.android.synthetic.main.fragment_filters.*
import java.util.*

class FiltersFragment : BaseFragment() {

    private lateinit var filtersViewModel: FiltersFragmentViewModel
    private lateinit var eventsViewModel: EventsViewModel

    private lateinit var viewBinding: FragmentFiltersBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_filters, container, false)
        viewBinding.vm = filtersViewModel
        viewBinding.eventsVM = eventsViewModel
        viewBinding.ctx = context
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDateEditText()
        setUpSpinners()
        setPreviousSelections()
        observeApplyButton()
    }

    private fun initViewModel() {
        filtersViewModel = ViewModelProviders.of(this).get(FiltersFragmentViewModel::class.java)
        eventsViewModel = ViewModelProviders.of(activity!!).get(EventsViewModel::class.java)
    }

    private fun initDateEditText() {
        dateEditText.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setUpSpinners() {
        val categoryAdapter = ArrayAdapter.createFromResource(context, R.array.event_categories, android.R.layout.simple_spinner_item)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        val sortingTypeAdapter = ArrayAdapter.createFromResource(context, R.array.sorting_types, android.R.layout.simple_spinner_item)
        sortingTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortingSpinner.adapter = sortingTypeAdapter
    }

    private fun setPreviousSelections() {
        filtersViewModel.setPreviousSelectionsIfNeeded(eventsViewModel, viewBinding)
    }

    private fun observeApplyButton() {
        filtersViewModel.applyButtonClick.observe(this, Observer {
            viewBinding.applyFiltersButton.findNavController().popBackStack()
        })
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, chosenYear, chosenMonth, chosenDay ->
            filtersViewModel.filterYear = chosenYear
            filtersViewModel.filterMonth = chosenMonth + 1
            filtersViewModel.filterDay = chosenDay
            dateEditText.setText(DateUtils.formatDate(chosenYear, chosenMonth + 1, chosenDay))
        }, year, month, day).show()
    }

    override fun getLayoutId() = R.layout.fragment_filters

    override fun getToolbar(): Toolbar = toolbar

    override fun getToolbarTitle(): String {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return context?.getString(R.string.title_fragment_filters) ?: ""
    }

    override fun shouldBottomNavBeVisible() = false

    override fun getFAB(): FloatingActionButton? = null

    override fun getFABAction(): () -> Unit = {}

    override fun isUsingDataBinding() = false
}