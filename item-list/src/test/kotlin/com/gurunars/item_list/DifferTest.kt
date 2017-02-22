package com.gurunars.item_list

import org.junit.Test

import java.util.Arrays

import org.junit.Assert.assertEquals


class DifferTest {

    @Test
    @Throws(Exception::class)
    fun itemCreate_shouldProduceSingleItemListWithCreation() {
        assertEquals(
                listOf(ChangeCreate(TestItem(7, 1), 6, 6)),
                getDiff(
                        Arrays.asList(
                                TestItem(1, 1),
                                TestItem(2, 1),
                                TestItem(3, 1),
                                TestItem(4, 1),
                                TestItem(5, 1),
                                TestItem(6, 1)
                        ),
                        Arrays.asList(
                                TestItem(1, 1),
                                TestItem(2, 1),
                                TestItem(3, 1),
                                TestItem(4, 1),
                                TestItem(5, 1),
                                TestItem(6, 1),
                                TestItem(7, 1)
                        ))
        )

    }

    @Test
    @Throws(Exception::class)
    fun itemDelete_shouldProduceSingleItemListWithRemoval() {
        assertEquals(
                listOf(ChangeDelete(TestItem(3, 1), 2, 2)),
                getDiff(
                        Arrays.asList(
                                TestItem(1, 1),
                                TestItem(2, 1),
                                TestItem(3, 1),
                                TestItem(4, 1),
                                TestItem(5, 1),
                                TestItem(6, 1)
                        ),
                        Arrays.asList(
                                TestItem(1, 1),
                                TestItem(2, 1),
                                TestItem(4, 1),
                                TestItem(5, 1),
                                TestItem(6, 1)
                        ))
        )

    }

    @Test
    @Throws(Exception::class)
    fun moveUp_shouldProduceSingleItemList() {
        assertEquals(
                listOf(ChangeMove(TestItem(5, 1), 4, 1)),
                getDiff(
                        Arrays.asList(
                                TestItem(1, 1),
                                TestItem(2, 1),
                                TestItem(3, 1),
                                TestItem(4, 1),
                                TestItem(5, 1),
                                TestItem(6, 1)
                        ),
                        Arrays.asList(
                                TestItem(1, 1),
                                TestItem(5, 1), // moved up
                                TestItem(2, 1),
                                TestItem(3, 1),
                                TestItem(4, 1),
                                TestItem(6, 1)
                        ))
        )

    }

    @Test
    @Throws(Exception::class)
    fun moveDown_shouldProduceSingleItemList() {
        assertEquals(
                listOf(ChangeMove(TestItem(2, 1), 1, 4)),
                getDiff(
                        Arrays.asList(
                                TestItem(1, 1),
                                TestItem(2, 1),
                                TestItem(3, 1),
                                TestItem(4, 1),
                                TestItem(5, 1),
                                TestItem(6, 1)
                        ),
                        Arrays.asList(
                                TestItem(1, 1),
                                TestItem(3, 1),
                                TestItem(4, 1),
                                TestItem(5, 1),
                                TestItem(2, 1), // moved down
                                TestItem(6, 1)
                        ))
        )
    }

    @Test
    @Throws(Exception::class)
    fun itemEdit_shouldProduceSingleItemListWithUpdate() {
        assertEquals(
                listOf(ChangeUpdate(TestItem(3, 2), 2, 2)),
                getDiff(
                        Arrays.asList(
                                TestItem(1, 1),
                                TestItem(2, 1),
                                TestItem(3, 1),
                                TestItem(4, 1),
                                TestItem(5, 1),
                                TestItem(6, 1)
                        ),
                        Arrays.asList(
                                TestItem(1, 1),
                                TestItem(2, 1),
                                TestItem(3, 2), // updated
                                TestItem(4, 1),
                                TestItem(5, 1),
                                TestItem(6, 1)
                        ))
        )

    }

    @Test
    @Throws(Exception::class)
    fun simpleDiff_isCorrect() {

        assertEquals(
                Arrays.asList(
                        ChangeDelete(TestItem(5, 1), 4, 4),
                        ChangeDelete(TestItem(2, 1), 1, 1),
                        ChangeCreate(TestItem(7, 2), 0, 0),
                        ChangeCreate(TestItem(9, 1), 4, 4),
                        ChangeMove(TestItem(1, 1), 1, 3),
                        ChangeMove(TestItem(3, 1), 1, 2),
                        ChangeUpdate(TestItem(1, 2), 3, 3),
                        ChangeUpdate(TestItem(3, 2), 2, 2)
                ),
                getDiff(
                        Arrays.asList(
                                TestItem(1, 1),
                                TestItem(2, 1),
                                TestItem(3, 1),
                                TestItem(4, 1),
                                TestItem(5, 1)
                        ),
                        Arrays.asList(
                                TestItem(7, 2),
                                TestItem(4, 1),
                                TestItem(3, 2),
                                TestItem(1, 2),
                                TestItem(9, 1)
                        ))
        )

    }

    @Test
    fun tooManyMoves_resultInComplexPermutation() {

        assertEquals(
                listOf(ChangeComplexPermutation(1, Arrays.asList(
                        TestItem(7, 1),
                        TestItem(5, 1),
                        TestItem(8, 1),
                        TestItem(6, 1),
                        TestItem(4, 1),
                        TestItem(2, 1),
                        TestItem(3, 1)
                ))),
                getDiff(
                        Arrays.asList(
                                TestItem(1, 1),
                                TestItem(2, 1),
                                TestItem(3, 1),
                                TestItem(4, 1),
                                TestItem(5, 1),
                                TestItem(6, 1),
                                TestItem(7, 1),
                                TestItem(8, 1),
                                TestItem(9, 1)
                        ),
                        Arrays.asList(
                                TestItem(1, 1),
                                TestItem(7, 1),
                                TestItem(5, 1),
                                TestItem(8, 1),
                                TestItem(6, 1),
                                TestItem(4, 1),
                                TestItem(2, 1),
                                TestItem(3, 1),
                                TestItem(9, 1)
                        ))
        )

    }

}