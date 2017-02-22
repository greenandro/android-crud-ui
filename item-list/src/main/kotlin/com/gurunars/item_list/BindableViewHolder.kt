package com.gurunars.item_list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

internal class BindableViewHolder<ItemType : Item> : RecyclerView.ViewHolder {

    private var itemViewBinder: ItemViewBinder<ItemType>? = null

    constructor(root: ViewGroup,
                itemViewBinder: ItemViewBinder<ItemType>) : super(itemViewBinder.getView(root.context)) {
        this.itemViewBinder = itemViewBinder
        itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    constructor(root: ViewGroup,
                emptyViewBinder: EmptyViewBinder) : super(emptyViewBinder.getView(root.context)) {
        this.itemViewBinder = null
        itemView.setTag(ItemViewBinderEmpty.EMPTY_TYPE, ItemViewBinderEmpty.EMPTY_TYPE)
        itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }

    fun bind(item: ItemType, previousItem: ItemType?) {
        if (itemViewBinder != null) {
            (itemViewBinder as ItemViewBinder<ItemType>).bind(itemView, item, previousItem)
        }
    }
}
