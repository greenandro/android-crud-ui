package com.gurunars.item_list

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.widget.FrameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * @param <ItemType> subclass of the Item instances of which are used to populate the views
 */
class ItemList<ItemType : Item> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val itemAdapter: ItemAdapter<ItemType>
    private val layoutManager: LinearLayoutManager

    init {

        val recyclerView = recyclerView {
            id=R.id.recyclerView
            lparams(width= matchParent,
                    height=matchParent)
        }

        layoutManager = LinearLayoutManager(context)
        itemAdapter = ItemAdapter<ItemType>(object : Scroller {
            override fun scrollToPosition(position: Int) {
                layoutManager.scrollToPosition(position)
            }

            override fun findFirstVisibleItemPosition(): Int {
                return layoutManager.findFirstVisibleItemPosition()
            }

            override fun findLastVisibleItemPosition(): Int {
                return layoutManager.findLastVisibleItemPosition()
            }
        })

        recyclerView.adapter = itemAdapter
        recyclerView.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.VERTICAL

    }

    /**
     * Set a collection of items to be shown.
     *
     * If the collection of items is different - the diff shall be animated.
     *
     * @param items a new collection to be shown
     */
    fun setItems(items: List<ItemType>) {
        itemAdapter.setItems(items)
    }

    /**
     * Map item type to view binder responsible for rending items of this type.
     *
     * @param itemType type of the Item
     * @param itemViewBinder renderer for the items of a given type
     */
    fun registerItemViewBinder(itemType: Enum<*>, itemViewBinder: ItemViewBinder<ItemType>) {
        itemAdapter.registerItemViewBinder(itemType, itemViewBinder)
    }

    /**
     * Set the renderer to be employed when the list contains no items.
     *
     * @param emptyViewBinder renderer for the empty list
     */
    fun setEmptyViewBinder(emptyViewBinder: EmptyViewBinder) {
        itemAdapter.setEmptyViewBinder(emptyViewBinder)
    }

}
