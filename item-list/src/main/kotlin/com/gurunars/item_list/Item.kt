package com.gurunars.item_list

import java.io.Serializable

/**
 * Abstraction of the entity that can be shown in the ItemListView.
 *
 * "equals" method MUST be implemented to compare items subclasses by value.
 */
interface Item : Serializable, Cloneable {

    /**
     * @return value to be used when deciding which view to use to render the items in a
     * *         RecyclerView
     */
    val type: Enum<*>

    /**
     * @return value to differentiate one item from another within a RecyclerView
     */
    val id: Long

    /**
     * @param other another Item
     * *
     * @return true if the items are identical by value, false otherwise
     */
    override fun equals(other: Any?): Boolean
}
