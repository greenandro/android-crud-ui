package com.gurunars.crud_item_list.example

import com.gurunars.item_list.Item


fun <ItemType : Item> setItem(items: MutableList<ItemType>, item: ItemType): List<ItemType> {
    val index = items.indexOfFirst { it.id == item.id }
    if (index == -1) {
        items.add(item)
    } else {
        items[index] = item
    }
    return items
}
