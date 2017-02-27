package com.gurunars.floatmenu.example

import android.content.pm.ActivityInfo
import android.support.test.espresso.ViewInteraction
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @Rule @JvmField
    val mActivityRule = ActivityTestRule(MainActivity::class.java)

    private fun restart() {
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        mActivityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    private fun fab(): ViewInteraction {
        return onView(withId(R.id.openFab))
    }

    private fun checkAlert(title: String) {
        onView(withId(R.id.alertTitle)).check(matches(withText(title)))
    }

    private fun checkFab(contentDescription: String) {
        fab().check(matches(withContentDescription(contentDescription)))
    }

    @Test
    fun clickingFab_shouldOpenAndCloseMenu() {
        restart()
        checkFab("|BG:-7667712|IC:-1|ACT:false|LH:false")
        fab().perform(click())
        restart()
        checkFab("|BG:-1|IC:-16777216|ACT:true|LH:false")
        fab().perform(click())
        restart()
        checkFab("|BG:-7667712|IC:-1|ACT:false|LH:false")
    }

    @Test
    fun clickingText_shouldShowAlertDialog() {
        onView(withId(R.id.textView)).perform(click())
        checkAlert("Text Clicked")
    }

    @Test
    fun clickingButtonInMenu_shouldWorkAsExpected() {
        restart()
        fab().perform(click())
        restart()
        onView(withId(R.id.button)).perform(click())
        checkAlert("Button Clicked")
        onView(withId(R.id.textView)).check(doesNotExist())
    }

    @Test
    fun togglingMenuDecoration_shouldChangeBackgroundAndForeground() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext())
        onView(withText("Toggle button color")).perform(click())
        restart()
        checkFab("|BG:-4419697|IC:-16777216|ACT:false|LH:false")
    }

    private fun disableBg() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext())
        onView(withText("Disable background")).perform(click())
        fab().perform(click())
    }

    @Test
    fun disablingBackground_shouldMakeBackgroundTranslucent() {
        disableBg()
        restart()
        onView(withId(R.id.textView)).perform(click())
        checkAlert("Text Clicked")
    }

    @Test
    fun disablingBackground_shouldMakeButtonClickable() {
        disableBg()
        restart()
        onView(withId(R.id.button)).perform(click())
        checkAlert("Button Clicked")
        onView(withId(android.R.id.button1)).perform(click())
    }

    @Test
    fun disablingBackground_shouldLeaveButtonBgClickable() {
        disableBg()
        restart()
        onView(withId(R.id.buttonFrame)).perform(click())
        checkAlert("Button Frame Clicked")
        onView(withId(android.R.id.button1)).perform(click())
    }

    @Test
    fun togglingLeftHand_shouldChangeTheProperties() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext())
        onView(withText("Set left handed")).perform(click())
        restart()
        checkFab("|BG:-7667712|IC:-1|ACT:false|LH:true")
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext())
        onView(withText("Set right handed")).perform(click())
        restart()
        checkFab("|BG:-7667712|IC:-1|ACT:false|LH:false")
    }

}