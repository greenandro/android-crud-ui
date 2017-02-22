package com.gurunars.item_list

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.ViewGroup

import com.esotericsoftware.kryo.Kryo

import org.objenesis.strategy.StdInstantiatorStrategy

import java.util.ArrayList


internal class ItemAdapter<ItemType : Item>(private val scroller: Scroller) : RecyclerView.Adapter<BindableViewHolder<ItemType>>() {

    private val kryo = Kryo()
    private var items: MutableList<ItemType> = ArrayList()
    private var previousList: List<ItemType> = ArrayList()

    private var emptyViewBinder: EmptyViewBinder = ItemViewBinderEmpty()

    init {
        this.kryo.instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
        setEmptyViewBinder(ItemViewBinderEmpty())
    }

    private val defaultViewBinder = ItemViewBinderString<ItemType>()

    private val itemViewBinderMap = object : SparseArray<ItemViewBinder<ItemType>>() {
        init {
            put(ItemViewBinderFooter.FOOTER_TYPE, ItemViewBinderFooter<ItemType>())
        }
    }

    fun setEmptyViewBinder(emptyViewBinder: EmptyViewBinder) {
        this.emptyViewBinder = emptyViewBinder
    }

    fun registerItemViewBinder(anEnum: Enum<*>,
                               itemViewBinder: ItemViewBinder<ItemType>) {
        itemViewBinderMap.put(anEnum.ordinal, itemViewBinder)
    }

    fun setItems(newItems: List<ItemType>) {
        // make sure that item lists are passed by value
        previousList = kryo.copy(items)
        val newItemsCopy = kryo.copy(newItems)

        if (items.isEmpty()) {
            this.items.addAll(newItemsCopy)
            notifyDataSetChanged()
        } else {
            var position = -1

            for (change in getDiff(items, newItemsCopy)) {
                position = change.apply(this, scroller, items, position)
            }

            if (position >= 0) {
                scroller.scrollToPosition(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): BindableViewHolder<ItemType> {
        if (viewType == ItemViewBinderEmpty.EMPTY_TYPE) {
            return BindableViewHolder(parent, emptyViewBinder)
        } else {
            val itemViewBinder = this.itemViewBinderMap.get(viewType)
            return BindableViewHolder(parent, itemViewBinder ?: defaultViewBinder)
        }
    }

    override fun onBindViewHolder(holder: BindableViewHolder<ItemType>, position: Int) {
        if (position == items.size) {
            return   // nothing to bind
        }

        val item = items[position]
        var previousItem: ItemType? = null
        for (cursor in previousList) {
            if (item.id == cursor.id) {
                previousItem = cursor
                break
            }
        }

        holder.bind(item, previousItem)

    }

    override fun getItemViewType(position: Int): Int {
        if (items.size == 0) {
            return ItemViewBinderEmpty.EMPTY_TYPE
        }
        if (position == items.size) {
            return ItemViewBinderFooter.FOOTER_TYPE
        }
        return items[position].type.ordinal
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

}
