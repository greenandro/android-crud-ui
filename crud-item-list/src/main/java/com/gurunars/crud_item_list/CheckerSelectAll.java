package com.gurunars.crud_item_list;

import java.util.List;
import java.util.Set;

class CheckerSelectAll<ItemType> implements Checker<ItemType> {

    @Override
    public Boolean apply(List<ItemType> all, Set<ItemType> selectedItems) {
        return selectedItems.size() < all.size();
    }

}
