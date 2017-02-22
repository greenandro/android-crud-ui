package com.gurunars.item_list.example


import com.gurunars.item_list.Item

internal class AnimalItem(override val id: Long, override val type: AnimalItem.Type) : Item {

    enum class Type {
        MONKEY, TIGER, WOLF, LION
    }

    private var version: Int = 0

    fun update() {
        this.version++
    }

    override fun equals(obj: Any?): Boolean {
        if (obj !is AnimalItem) {
            return false
        }
        return id == obj.id && type == obj.type && version == obj.version
    }

    override fun toString(): String {
        return "" + id + " @ " + version
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + version
        return result
    }
}
