package com.decagon.weather

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.decagon.weather.adapter.ForecastListAdapter
import com.decagon.weather.view.MainActivity
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class FavouriteListTest {

    @get:Rule
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testRecyclerViewHasAtLeastOneItem() {
        onView(withId(R.id.forecast))
            .perform(RecyclerViewActions
                .actionOnItemAtPosition<ForecastListAdapter.UsersListAdapterViewHolder>(0, clickItemWithId(R.id.item_parent_layout)))
    }

    @Test
    fun testRecyclerViewHasMoreItemsAfterApiCall() {
        onView(isRoot()).perform(delayByMilliseconds(10000))

        onView(withId(R.id.forecast))
            .perform(RecyclerViewActions
                .actionOnItemAtPosition<ForecastListAdapter.UsersListAdapterViewHolder>(5, clickItemWithId(R.id.item_parent_layout)))
    }

    private fun delayByMilliseconds(delay: Long): ViewAction {
        return object : ViewAction {
            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(delay)
            }

            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for " + delay + "milliseconds"
            }
        }
    }

    private fun clickItemWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id) as View
                v.performClick()
            }
        }
    }
}