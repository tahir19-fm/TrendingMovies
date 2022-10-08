package com.example.trendingrepos

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trendingrepos.utils.EspressoIdlingResource
import org.hamcrest.Description
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore


@RunWith(AndroidJUnit4::class)
class RecyclerViewTest {
    @Before
    fun setUp() {
        BuildConfig.IS_TESTING.set(true)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }
    @Test
    fun useAppContext() {
        assertTrue(verifyRepoNameInRecyclerView("truth"))
    }
    @Ignore
    fun verifyRepoNameInRecyclerView(name: String): Boolean {
        //check if there are schedules in the list
        if (itemsCount(R.id.rv_root) > 0) {
            Espresso.onView(withId(R.id.rv_root)).check(
                ViewAssertions.matches((CustomAssertions.hasItem(
                    ViewMatchers.hasDescendant(ViewMatchers.withText(name))
                ))))
            return true
        }
        return false
    }
    @Ignore
    fun itemsCount(@IdRes recyclerViewID: Int): Int {
        var count = 0
        val matcher = object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
            }

            override fun matchesSafely(item: View?): Boolean {
                count = (item as RecyclerView).adapter!!.itemCount
                return true
            }

        }
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(recyclerViewID),
                ViewMatchers.isDisplayed()
            )
        ).check(ViewAssertions.matches(matcher))
        val result = count
        count = 0
        return result
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

}