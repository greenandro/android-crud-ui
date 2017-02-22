package com.gurunars.crud_item_list.example

import com.gurunars.item_list.Item


internal class AnimalItem(override var id: Long, override val type: AnimalItem.Type) : Item {

    internal enum class Type {
        MONKEY, TIGER, WOLF, LION
    }

    var version: Int = 0

    constructor(type: Type) : this(0, type) {}

    override fun hashCode(): Int {
        return java.lang.Long.valueOf(id)!!.hashCode()
    }

    override fun equals(obj: Any?): Boolean {
        if (obj !is AnimalItem) {
            return false
        }

        return id == obj.id && type == obj.type && version == obj.version
    }

    override fun toString(): String {
        return "" + id + " @ " + version + " [" + type.name.toLowerCase() + "]"
    }
}