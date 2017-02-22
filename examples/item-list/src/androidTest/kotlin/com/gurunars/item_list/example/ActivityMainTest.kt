package com.gurunars.item_list.example

import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.hasDescendant
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText

@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityMainTest {

    @Rule @JvmField
    var mActivityRule = ActivityTestRule(ActivityMain::class.java)

    private fun clickMenu(text: String) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(text)).perform(click())
    }

    @Test
    fun clickingClear_shouldShowEmptyListView() {
        clickMenu("Clear")
        onView(withText("Empty")).check(matches(isDisplayed()))
    }

    private fun assertList(vararg expectedItems: String) {
        for (i in expectedItems.indices) {
            nthChildOf(withId(R.id.itemList), i).matches(
                    hasDescendant(withText(expectedItems[i])))
        }
    }

    @Test
    fun deletingItems_shouldLeadToPartialRemoval() {
        clickMenu("Delete items")
        assertList(
                "0 @ 0 [tiger]",
                "2 @ 0 [monkey]"
        )
    }

    @Test
    fun createItems_shouldAppendItemsToTheEnd() {
        clickMenu("Create items")
        assertList(
                "0 @ 0 [tiger]",
                "1 @ 0 [wolf]",
                "2 @ 0 [monkey]",
                "3 @ 0 [lion]",
                "4 @ 0 [tiger]",
                "5 @ 0 [wolf]",
                "6 @ 0 [monkey]",
                "7 @ 0 [lion]"
        )
    }

    @Test
    fun updateItems_shouldChangeSomeOfItems() {
        clickMenu("Update items")
        assertList(
                "0 @ 0 [tiger]",
                "1 @ 1 [wolf]",
                "2 @ 0 [monkey]",
                "3 @ 1 [lion]"
        )
    }

    @Test
    fun moveUp_shouldPutItemFromBottomToTop() {
        clickMenu("Move up")
        assertList(
                "3 @ 0 [lion]",
                "0 @ 0 [tiger]",
                "1 @ 0 [wolf]",
                "2 @ 0 [monkey]"
        )
    }

    @Test
    fun moveDown_shouldPutItemFromTopToBottom() {
        clickMenu("Move down")
        assertList(
                "1 @ 0 [wolf]",
                "2 @ 0 [monkey]",
                "3 @ 0 [lion]",
                "0 @ 0 [tiger]"
        )
    }

    @Test
    fun resetItems_shouldSetItemsToInitialList() {
        clickMenu("Reset items")
        assertList(
                "0 @ 0 [tiger]",
                "1 @ 0 [wolf]",
                "2 @ 0 [monkey]",
                "3 @ 0 [lion]"
        )
    }

    @Before
    fun before() {
        clickMenu("Reset items")
    }

    @After
    fun after() {
        clickMenu("Reset items")
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