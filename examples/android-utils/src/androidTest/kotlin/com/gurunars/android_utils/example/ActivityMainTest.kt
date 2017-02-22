package com.gurunars.android_utils.example

import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId


@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityMainTest {

    @Rule @JvmField
    var mActivityRule = ActivityTestRule<ActivityMain>(
            ActivityMain::class.java)

    @Test
    fun clickingButtons_shouldNotProduceExceptions() {
        onView(withId(R.id.clear)).perform(click())
        onView(withId(R.id.disabled)).perform(click())
        onView(withId(R.id.set)).perform(click())
    }

}