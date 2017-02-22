package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

internal class ConcreteSelectableItem<ItemType : Item>(override val item: ItemType, override val isSelected: Boolean) : SelectableItem<ItemType> {

    override val type: Enum<*>
        get() = item.type

    override val id: Long
        get() = item.id

    override fun hashCode(): Int {
        return id.toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ConcreteSelectableItem<*>) {
            return false
        }
        return item == other.item && isSelected == other.isSelected
    }

    override fun toString(): String {
        return item.toString() + " | " + isSelected
    }
}
