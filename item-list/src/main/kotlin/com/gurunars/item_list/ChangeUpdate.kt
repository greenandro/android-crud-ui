package com.gurunars.item_list

import android.support.v7.widget.RecyclerView

import javax.inject.Inject

internal class ChangeUpdate<ItemType : Item>(item: ItemType, sourcePosition: Int, targetPosition: Int)
    : ChangeOfPart<ItemType>(item, sourcePosition, targetPosition) {

    @Inject
    private val getPosition = ::getScrollPosition

    override fun apply(adapter: RecyclerView.Adapter<*>, scroller: Scroller, items: MutableList<ItemType>,
                       currentPosition: Int): Int {
        items[sourcePosition] = item
        adapter.notifyItemChanged(sourcePosition)
        return getPosition(sourcePosition, scroller, items)
    }
}
