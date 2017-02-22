package com.gurunars.leaflet_view.example

import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.core.AllOf.allOf

@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityMainTest {

    @Rule @JvmField
    var mActivityRule = ActivityTestRule(
            ActivityMain::class.java)

    private fun createPage(title: String) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Create page")).perform(click())
        onView(allOf(isDisplayed(), withId(R.id.pageTitle))).perform(replaceText(title))
        onView(withId(android.R.id.button1)).perform(click())
    }

    private fun checkTitle(text: String) {
        onView(allOf(isDisplayed(), withId(R.id.pageTitle))).check(matches(withText(text)))
        onView(nthChildOf(withId(R.id.action_bar), 0)).check(matches(withText(text)))
    }

    private fun ensureEmpty() {
        onView(withId(R.id.noPageText)).check(matches(withText("Empty")))
        onView(nthChildOf(withId(R.id.action_bar), 0)).check(matches(withText("Empty")))
    }

    @Test
    fun initialOpen_shouldShowEmptyView() {
        ensureEmpty()
    }

    @Test
    fun creatingBlankNewPage_shouldNavigateToIt() {
        createPage("Page title")
        checkTitle("Page title")
    }

    @Test
    fun creatingNewPage_shouldNotNavigateAnywhereToIt() {
        createPage("Page title")
        createPage("aaa")
        createPage("zzz")
        checkTitle("Page title")
    }

    @Test
    fun deletingTheOnlyPage_shouldShowEmptyView() {
        createPage("Page title")
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Delete page")).perform(click())
        ensureEmpty()
    }

    @Test
    fun editingThePage_shouldChangeIt() {
        createPage("Page title")
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Edit page")).perform(click())
        onView(withId(R.id.pageTitle)).perform(replaceText("Edited title"))
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.pageTitle)).check(matches(withText("Edited title")))
        checkTitle("Edited title")
    }

    @Test
    fun goingToPage_shouldNavigateToIt() {
        createPage("Page title 1")
        createPage("Page title 2")
        createPage("Page title 3")
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Go to page")).perform(click())
        onView(withText("Page title 1")).perform(click())
        onView(nthChildOf(withId(R.id.action_bar), 0)).check(matches(withText("Page title 1")))
    }

    @Before
    fun clear() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Clear")).perform(click())
    }

    companion object {

        fun nthChildOf(parentMatcher: Matcher<View>, childPosition: Int): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description) {
                    description.appendText("position $childPosition of parent ")
                    parentMatcher.describeTo(description)
                }

                public override fun matchesSafely(view: View): Boolean {
                    if (view.parent !is ViewGroup) return false
                    val parent = view.parent as ViewGroup

                    return parentMatcher.matches(parent)
                            && parent.childCount > childPosition
                            && parent.getChildAt(childPosition) == view
                }
            }
        }
    }

}