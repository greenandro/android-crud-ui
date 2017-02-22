package com.gurunars.item_list

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

import java.util.Arrays

import org.junit.Assert.assertEquals
import org.mockito.Mockito.`when`

class ScrollPositionFetcherTest {

    private val scroller = Mockito.mock(Scroller::class.java)
    private val items = Arrays.asList(
            TestItem(0, 0),
            TestItem(1, 0),
            TestItem(2, 0),
            TestItem(3, 0),
            TestItem(4, 0),
            TestItem(5, 0),
            TestItem(6, 0),
            TestItem(7, 0),
            TestItem(8, 0),
            TestItem(9, 0),
            TestItem(10, 0),
            TestItem(11, 0),
            TestItem(12, 0),
            TestItem(13, 0),
            TestItem(14, 0)
    )

    @Before
    fun setup() {
        `when`(scroller.findFirstVisibleItemPosition()).thenReturn(3) // upper bound @ 6
        `when`(scroller.findLastVisibleItemPosition()).thenReturn(12) // lower bound @ 9
    }

    @Test
    fun ifItemIsInVisibleRange_doNotScroll() {
        assertEquals(-1, getScrollPosition(7, scroller, items))
    }

    @Test
    fun ifItemIsAboveTop_scrollThreeStepsAbovePosition() {
        assertEquals(2, getScrollPosition(5, scroller, items))
    }

    @Test
    fun ifItemIsBelowBott_scrollThreeStepsBelowPosition() {
        assertEquals(13, getScrollPosition(10, scroller, items))
    }

    @Test
    fun ifItemIsWayAboveTheTop_scollToZero() {
        assertEquals(0, getScrollPosition(-1, scroller, items))
    }

    @Test
    fun ifItemIsWayBelowTheBottom_scollToBottom() {
        assertEquals(14, getScrollPosition(50, scroller, items))
    }

}
