package com.gurunars.crud_item_list;

import java.util.List;
import java.util.Set;

public interface Action<ItemType> {

    void perform(List<ItemType> all, Set<ItemType> selectedItems);
    boolean canPerform(List<ItemType> all, Set<ItemType> selectedItems);

}
