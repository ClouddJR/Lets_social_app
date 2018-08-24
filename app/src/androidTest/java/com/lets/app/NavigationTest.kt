package com.lets.app

import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.lets.app.activities.MainActivity
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val mainActivityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun shouldHomeButtonClickNavigateToHomeFragment() {
        onView(withId(R.id.homeButton)).perform(click())
        onView(withId(R.id.toolbar)).check(matches(withToolbarTitle(`is`("Home"))))
        onView(withId(R.id.fab)).check(matches(not(isDisplayed())))
    }

    @Test
    fun shouldExploreButtonClickNavigateToExploreFragment() {
        onView(withId(R.id.exploreButton)).perform(click())
        onView(withId(R.id.toolbar)).check(matches(withToolbarTitle(`is`("Explore"))))
        onView(withId(R.id.fab)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldProfileButtonClickNavigateToProfileFragment() {
        onView(withId(R.id.profileButton)).perform(click())
        onView(withId(R.id.toolbar)).check(matches(withToolbarTitle(`is`("Profile"))))
        onView(withId(R.id.fab)).check(matches(not(isDisplayed())))
    }

    @Test
    fun shouldMessagesButtonClickNavigateToMessagesFragment() {
        onView(withId(R.id.messagesButton)).perform(click())
        onView(withId(R.id.toolbar)).check(matches(withToolbarTitle(`is`("Messages"))))
        onView(withId(R.id.fab)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldPlusIconClickNavigateToAddEventFragment() {
        onView(withId(R.id.plusIcon)).perform(click())
        onView(withId(R.id.toolbar)).check(matches(withToolbarTitle(`is`("New event"))))
        onView(withId(R.id.fab)).check(matches(not(isDisplayed())))
    }

    @Test
    fun shouldFabButtonClickNavigateToMapFragment() {
        onView(withId(R.id.exploreButton)).perform(click())
        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.mapLayout)).check(matches(isDisplayed()))
    }


    private fun withToolbarTitle(textMatcher: Matcher<String>): Matcher<Any> {
        return object : BoundedMatcher<Any, Toolbar>(Toolbar::class.java) {
            public override fun matchesSafely(toolbar: Toolbar): Boolean {
                return textMatcher.matches(toolbar.title)
            }

            override fun describeTo(description: Description) {
                description.appendText("with toolbar title: ")
                textMatcher.describeTo(description)
            }
        }
    }

}