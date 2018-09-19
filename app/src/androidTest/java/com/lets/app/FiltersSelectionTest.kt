package com.lets.app

import android.widget.EditText
import android.widget.Spinner
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.lets.app.activities.MainActivity
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FiltersSelectionTest {

    @get:Rule
    val mainActivityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    private lateinit var sportCategory: String
    private lateinit var allCategory: String
    private lateinit var sortingType: String

    private lateinit var selectedDateString: String

    @Before
    fun setup() {
        allCategory = mainActivityTestRule.activity.resources.getStringArray(R.array.event_categories)[0]
        sportCategory = mainActivityTestRule.activity.resources.getStringArray(R.array.event_categories)[1]
        sortingType = mainActivityTestRule.activity.resources.getStringArray(R.array.sorting_types)[1]
    }

    @Test
    fun shouldPreserveFiltersAndSortingSelectionWithDate() {

        navigateToFilterFragment()
        selectDate()
        selectFilterAndSortType()
        clickApplyButton()
        navigateToFilterFragment()

        onView(withId(R.id.categorySpinner)).check(matches(withSpinnerTitle(`is`(sportCategory))))
        onView(withId(R.id.sortingSpinner)).check(matches(withSpinnerTitle(`is`(sortingType))))
        onView(withId(R.id.dateEditText)).check(matches(withText(selectedDateString)))
    }

    @Test
    fun shouldPreserveFiltersAndSortingSelectionWithoutDate() {

        navigateToFilterFragment()
        selectFilterAndSortType()
        clickApplyButton()
        navigateToFilterFragment()

        onView(withId(R.id.categorySpinner)).check(matches(withSpinnerTitle(`is`(sportCategory))))
        onView(withId(R.id.sortingSpinner)).check(matches(withSpinnerTitle(`is`(sortingType))))
        onView(withId(R.id.dateEditText)).check(matches(not(isDisplayed())))
    }

    @Test
    fun shouldResetFiltersAfterResetButtonClick() {

        navigateToFilterFragment()
        selectDate()
        clickApplyButton()
        navigateToFilterFragment()
        clickResetButton()
        clickApplyButton()
        navigateToFilterFragment()

        onView(withId(R.id.dateEditText)).check(matches(not(isDisplayed())))
    }

    private fun navigateToFilterFragment() {
        onView(withId(R.id.exploreButton)).perform(click())
        onView(withId(R.id.filtersButton)).perform(click())
    }

    private fun selectDate() {
        onView(withId(R.id.dateSelectedCheckbox)).perform(click())
        onView(withId(R.id.dateEditText)).perform(click())
        onView(withText("OK")).perform(click())

        selectedDateString = mainActivityTestRule.activity.findViewById<EditText>(R.id.dateEditText)
                .text.toString()
    }

    private fun selectFilterAndSortType() {
        onView(withId(R.id.categorySpinner)).perform(click())
        onView(withText(sportCategory)).perform(click())

        onView(withId(R.id.sortingSpinner)).perform(click())
        onView(withText(sortingType)).perform(click())
    }

    private fun clickResetButton() {
        onView(withId(R.id.resetFiltersButton)).perform(click())
    }

    private fun clickApplyButton() {
        onView(withId(R.id.applyFiltersButton)).perform(click())
    }

    private fun withSpinnerTitle(textMatcher: Matcher<String>): Matcher<Any> {
        return object : BoundedMatcher<Any, Spinner>(Spinner::class.java) {
            override fun matchesSafely(spinner: Spinner?): Boolean {
                return textMatcher.matches(spinner?.selectedItem)
            }

            override fun describeTo(description: Description?) {

            }
        }
    }
}