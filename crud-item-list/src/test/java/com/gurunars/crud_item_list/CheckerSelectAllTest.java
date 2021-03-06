package com.gurunars.crud_item_list;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CheckerSelectAllTest {

    private final CheckerSelectAll<String> checkerSelectAll = new CheckerSelectAll<>();
    private final List<String> all = Arrays.asList("one", "two");

    @Test
    public void noSelection_leadsToTrue() throws Exception {
        assertTrue(checkerSelectAll.apply(all, new HashSet<String>()));
    }

    @Test
    public void selectionOfAll_leadsToFalse() throws Exception {
        assertFalse(checkerSelectAll.apply(all, new HashSet<>(all)));
    }

}
