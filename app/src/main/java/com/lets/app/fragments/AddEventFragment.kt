package com.lets.app.fragments

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lets.app.R
import com.lets.app.activities.LocationPickerActivity
import com.lets.app.databinding.FragmentAddEventBinding
import com.lets.app.utils.CategoriesUtils.animateCategoryView
import com.lets.app.utils.CategoriesUtils.revertCategoryViewAnimation
import com.lets.app.utils.DateUtils.formatDate
import com.lets.app.utils.DateUtils.formatTime
import com.lets.app.utils.ExtFunctions.hasImageColor
import com.lets.app.viewmodels.AddEventFragmentViewModel
import kotlinx.android.synthetic.main.fragment_add_event.*
import java.util.*


class AddEventFragment : BaseFragment() {

    private lateinit var viewModel: AddEventFragmentViewModel

    private lateinit var googleMap: GoogleMap

    private val locationPickerRequest = 123

    private var clickedImage: ImageView? = null

    private var wasTimePickerAlreadyDisplayed = false
    private var reminderDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAddEventBinding>(inflater,
                R.layout.fragment_add_event, container, false)
        binding.vm = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        setUpMapView()
        setUpEditTexts()
        setUpRangeBars()
        setUpSexRadioBoxes()
        setUpRangeCheckBoxes()
        setUpCategoriesClickListeners()
        setUpAutoCompleteSearch()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(AddEventFragmentViewModel::class.java)
    }

    private fun setUpEditTexts() {
        dateEditText.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setUpRangeBars() {
        maxPeopleRangeBar.setOnRangeBarChangeListener { _, _, _, _, rightPinValue ->
            viewModel.maxPeople = rightPinValue.toInt()
        }

        ageRangeBar.setOnRangeBarChangeListener { _, _, _, leftPinValue, rightPinValue ->
            viewModel.ageFrom = leftPinValue.toInt()
            viewModel.ageTo = rightPinValue.toInt()
        }
    }

    private fun setUpSexRadioBoxes() {
        sexRadioGroup.setOnCheckedChangeListener { _, index ->
            viewModel.setEventSex(index)
        }
    }

    private fun setUpRangeCheckBoxes() {

        viewModel.maxPeopleCheckboxClick.observe(this, Observer { isChecked ->
            when (isChecked) {
                true -> maxPeopleRangeBar.visibility = View.GONE
                false -> {
                    maxPeopleRangeBar.visibility = View.VISIBLE
                    scrollToBottom()
                }
            }
        })

        viewModel.ageCheckboxClick.observe(this, Observer { isChecked ->
            when (isChecked) {
                true -> ageRangeBar.visibility = View.GONE
                false -> {
                    ageRangeBar.visibility = View.VISIBLE
                    scrollToBottom()
                }
            }
        })
    }

    private fun scrollToBottom() {
        mainScrollView.post {
            mainScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun setUpCategoriesClickListeners() {
        sportImageView.setOnClickListener(viewClickListener)
        cultureImageView.setOnClickListener(viewClickListener)
        entertainmentImageView.setOnClickListener(viewClickListener)
        recreationImageView.setOnClickListener(viewClickListener)
        learnImageView.setOnClickListener(viewClickListener)
        otherImageView.setOnClickListener(viewClickListener)
    }

    private val viewClickListener = View.OnClickListener {
        if (clickedImage == null) {
            clickedImage = it as ImageView
            animateCategoryView(it)
        } else if (!(it as ImageView).hasImageColor(R.color.colorPrimary)) {
            revertCategoryViewAnimation(clickedImage!!)
            clickedImage = it
            animateCategoryView(it)
        }
        viewModel.setEventCategory(clickedImage!!)
    }

    private fun setUpMapView() {
        mapView.getMapAsync {
            googleMap = it
            observeEventLocation()
        }
        mapView.isClickable = false
    }

    private fun setUpAutoCompleteSearch() {
        locationEditText.setOnClickListener {
            startActivityForResult(Intent(context, LocationPickerActivity::class.java), locationPickerRequest)
        }
    }

    private fun observeEventLocation() {
        viewModel.chosenEventLocation.observe(this, Observer {
            it?.let {
                mapView.visibility = View.VISIBLE
                googleMap.clear()
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
                googleMap.addMarker(MarkerOptions().position(it).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)))
                googleMap.uiSettings.setAllGesturesEnabled(false)
            }
        })

        viewModel.addressOfChosenLocation.observe(this, Observer {
            val address = it.first()
            Log.d("addressTag", address.toString())
            val addressString = """${address.locality}, ${address.getAddressLine(0).split(",")[0]}"""
            locationEditText.setText(addressString)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == locationPickerRequest && resultCode == RESULT_OK) {
            data?.let {
                val chosenLatLnG = LatLng(
                        it.getDoubleExtra("lat", 0.0),
                        it.getDoubleExtra("lng", 0.0)
                )
                viewModel.setEventLocation(chosenLatLnG)
                viewModel.getReadableInfoAboutLocation(context!!)
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        wasTimePickerAlreadyDisplayed = false
        DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, chosenYear, chosenMonth, chosenDay ->
            viewModel.year = chosenYear
            viewModel.month = chosenMonth + 1
            viewModel.day = chosenDay
            reminderDate = formatDate(chosenYear, chosenMonth + 1, chosenDay)
            if (!wasTimePickerAlreadyDisplayed) {
                displayTimePickerDialog()
            }
        }, year, month, day).show()
    }

    private fun displayTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        wasTimePickerAlreadyDisplayed = true
        TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, chosenHour, chosenMinute ->
            if (view.isShown) {
                viewModel.hour = chosenHour
                viewModel.minute = chosenMinute
                reminderDate += ", ${formatTime(chosenHour, chosenMinute)}"
                dateEditText.setText(reminderDate)
            }
        }, hour, minute, true).show()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_add_event
    }

    override fun getToolbar(): Toolbar {
        return toolbar
    }

    override fun getToolbarTitle(): String {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return context?.getString(R.string.title_fragment_add_event) ?: ""
    }

    override fun shouldBottomNavBeVisible(): Boolean {
        return false
    }

    override fun getFAB(): FloatingActionButton? {
        return null
    }

    override fun getFABAction(): () -> Unit {
        return {}
    }

    override fun isUsingDataBinding(): Boolean {
        return true
    }
}