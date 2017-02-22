package com.gurunars.item_list

import org.junit.Test

import java.util.Arrays

import org.junit.Assert.assertEquals

class FetcherComplexPermutationTest {

    @Test
    fun getNormal() {
        assertEquals(getComplexPermutation(
                ItemHolder.wrap(Arrays.asList(
                        TestItem(1, 1),
                        TestItem(2, 1),
                        TestItem(3, 1),
                        TestItem(4, 1)
                )),
                ItemHolder.wrap(Arrays.asList(
                        TestItem(1, 1),
                        TestItem(3, 1),
                        TestItem(2, 1),
                        TestItem(4, 1)
                ))),
                ChangeComplexPermutation(1, Arrays.asList(
                        TestItem(3, 1),
                        TestItem(2, 1)
                ))
        )
    }
}