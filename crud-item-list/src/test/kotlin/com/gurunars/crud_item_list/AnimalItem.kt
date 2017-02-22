package com.gurunars.crud_item_list


import com.gurunars.item_list.Item

internal class AnimalItem(override val id: Long, override val type: AnimalItem.Type) : Item {

    internal enum class Type {
        MONKEY, TIGER, WOLF, LION
    }

    override fun hashCode(): Int {
        return java.lang.Long.valueOf(id)!!.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AnimalItem) {
            return false
        }
        return id == other.id && type == other.type
    }

    override fun toString(): String {
        return "" + id + " [" + type.name.toLowerCase() + "]"
    }

}