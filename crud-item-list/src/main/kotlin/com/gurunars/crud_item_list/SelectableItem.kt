package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

/**
 * A wrapper around the item with a flag indicating if it was selected in the list view.
 *
 * @param <ItemType> type of the item
 * */
interface SelectableItem<ItemType : Item> : Item {

    /**
     * @return item inside the wrapper
     */
    val item: ItemType

    /**
     * @return true if the item was selected
     */
    val isSelected: Boolean

}
