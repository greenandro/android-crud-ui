package com.gurunars.item_list

internal class TestItem(override val id: Long, private val version: Int) : Item {

    enum class Type {ONE}

    override val type: Enum<*>
        get() = Type.ONE

    override fun toString(): String {
        return "new TestItem($id, $version)"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is TestItem) {
            return false
        }
        return id == other.id && version == other.version
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + version
        return result
    }
}
