package com.gurunars.crud_item_list;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class CheckerMoveUpTest {

    private final CheckerMoveUp<String> moveUpChecker = new CheckerMoveUp<>();
    private final List<String> all = Arrays.asList("one", "two", "three", "four");

    @Test
    public void selectingFirstItem_leadsToFalse() throws Exception {
        assertFalse(moveUpChecker.apply(all, Sets.newSet("one")));
    }

    @Test
    public void selectingInterruptedChunk_leadsToFalse() throws Exception {
        assertFalse(moveUpChecker.apply(all, Sets.newSet("two", "four")));
    }

    @Test
    public void selectingSolidChunkBeforeLast_leadsToTrue() throws Exception {
        assertTrue(moveUpChecker.apply(all, Sets.newSet("two", "three", "four")));
    }

}
