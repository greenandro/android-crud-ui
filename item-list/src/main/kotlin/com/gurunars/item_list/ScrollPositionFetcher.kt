package com.gurunars.item_list

internal fun getScrollPosition(position: Int, scroller: Scroller, items: List<*>): Int {
    var cursor = position
    val upperVisibilityThreshold = scroller.findFirstVisibleItemPosition() + 3
    val lowerVisibilityThreshold = scroller.findLastVisibleItemPosition() - 3

    if (upperVisibilityThreshold < cursor && cursor < lowerVisibilityThreshold) {
        return -1  // No change
    }

    cursor += if (cursor <= upperVisibilityThreshold) -3 else +3
    return Math.min(Math.max(cursor, 0), items.size - 1)
}

