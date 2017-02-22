package com.gurunars.crud_item_list

import android.content.Context
import android.view.View

import com.gurunars.item_list.Item
import com.gurunars.item_list.ItemViewBinder

internal class ClickableItemViewBinder<ItemType : Item>(
        private val itemViewBinder: ItemViewBinder<SelectableItem<ItemType>>,
        private val collectionManager: CollectionManager<ItemType>) : ItemViewBinder<SelectableItem<ItemType>> {

    override fun getView(context: Context): View {
        return itemViewBinder.getView(context)
    }

    override fun bind(itemView: View, item: SelectableItem<ItemType>,
                      previousItem: SelectableItem<ItemType>?) {
        itemViewBinder.bind(itemView, item, previousItem)

        itemView.setOnClickListener { collectionManager.itemClick(item.item) }
        itemView.setOnLongClickListener {
            collectionManager.itemLongClick(item.item)
            true
        }
    }
}
