package com.gurunars.item_list

import android.support.v7.widget.RecyclerView.Adapter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.internal.verification.VerificationModeFactory.times
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

@RunWith(PowerMockRunner::class)
@PrepareForTest(Adapter::class)
class ChangeTest {

    @Mock
    lateinit internal var adapter: Adapter<*>

    @Mock
    lateinit internal var scroller: Scroller

    @Mock
    lateinit internal var scroll : (position: Int, scroller: Scroller, items: List<*>) -> Int

    private val items = ArrayList<TestItem>()

    @Before
    fun setup() {
        items.clear()
    }

    private fun apply(change: Change<TestItem>): Int {
        return change.apply(adapter, scroller, items, -1)
    }

    @Test
    fun testChangeComplexPermutation() {
        items.addAll(Arrays.asList(
                TestItem(0, 0),
                TestItem(1, 0),
                TestItem(2, 0),
                TestItem(3, 0)
        ))
        val change = ChangeComplexPermutation(1,
                Arrays.asList(TestItem(2, 0), TestItem(3, 0), TestItem(1, 0)))
        assertEquals(-1, apply(change)) // TODO: verify via injection
        assertEquals(items, Arrays.asList(
                TestItem(0, 0),
                TestItem(2, 0),
                TestItem(3, 0),
                TestItem(1, 0)
        ))
        verify(adapter, times(1))!!.notifyItemRangeChanged(1, 3)
    }

    @Test
    fun testChangeCreate() {
        val change = ChangeCreate(TestItem(0, 0), 0, 0)
        assertEquals(0, apply(change)) // TODO: verify via injection
        assertEquals(items, listOf(TestItem(0, 0)))
        verify(adapter, times(1))!!.notifyItemInserted(0)
    }

    @Test
    fun testChangeDelete() {
        items.add(TestItem(0, 0))
        val change = ChangeDelete(TestItem(0, 0), 0, 0)
        assertEquals(-1, apply(change)) // TODO: verify via injection
        assertTrue(items.isEmpty())
        verify(adapter, times(1))!!.notifyItemRemoved(0)
    }

    @Test
    fun testChangeMove() {
        items.addAll(Arrays.asList(TestItem(0, 0), TestItem(1, 1)))
        val change = ChangeMove(TestItem(0, 0), 0, 1)
        assertEquals(0, apply(change)) // TODO: verify via injection
        assertEquals(items, Arrays.asList(TestItem(1, 1), TestItem(0, 0)))
        verify(adapter, times(1))!!.notifyItemMoved(0, 1)
    }

    @Test
    fun testChangeUpdate() {
        items.add(TestItem(0, 0))
        val change = ChangeUpdate(TestItem(0, 1), 0, 1)
        assertEquals(0, apply(change)) // TODO: verify via injection
        assertEquals(items, listOf(TestItem(0, 1)))
        verify(adapter, times(1))!!.notifyItemChanged(0)
    }

    @Test
    fun testChangePersist() {
        val change = ChangePersist<TestItem>()
        assertEquals(-1, apply(change))
    }

}
