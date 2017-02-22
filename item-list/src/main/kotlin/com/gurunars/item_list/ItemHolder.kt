package com.gurunars.item_list

import java.util.ArrayList

internal class ItemHolder<out ItemType : Item> constructor(val raw: ItemType) : Item {

    override val type: Enum<*>
        get() = raw.type

    override val id: Long
        get() = raw.id

    override fun equals(other: Any?): Boolean {
        return other != null &&
                this.javaClass == other.javaClass &&
                id == (other as Item).id
    }

    override fun hashCode(): Int {
        return java.lang.Long.valueOf(id)!!.hashCode()
    }

    companion object {
        fun <ItemType : Item> wrap(items: List<ItemType>): MutableList<ItemHolder<ItemType>> {
            return items.mapTo(ArrayList<ItemHolder<ItemType>>()) { ItemHolder(it) }
        }

        fun <ItemType : Item> unwrap(items: List<ItemHolder<ItemType>>): List<ItemType> {
            return items.map { it.raw }
        }
    }
}
