package com.gurunars.crud_item_list

import com.esotericsoftware.kryo.Kryo
import com.gurunars.item_list.Item
import org.objenesis.strategy.StdInstantiatorStrategy
import java.io.Serializable
import java.util.HashSet


internal class CollectionManager<ItemType : Item>(
        private val handleStateChange: (List<SelectableItem<ItemType>>) -> Unit,
        private val handleCollectionChange: (MutableList<ItemType>) -> Unit) : Serializable {

    private val kryo = Kryo()

    private var items = mutableListOf<ItemType>()
    private var selectedItems = mutableSetOf<ItemType>()
    private val selectedPositions : List<Int>
        get() = selectedItems.map {items.indexOf(it)}.sorted()

    private var itemConsumer: ((ItemType) -> Unit)? = null

    init {
        this.kryo.instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }

    private val selectableItems: List<SelectableItem<ItemType>>
        get() {
            return items.map { ConcreteSelectableItem(it, selectedItems.contains(it)) }
        }

    fun hasSelection(): Boolean {
        return !selectedItems.isEmpty()
    }

    fun canEdit(): Boolean {
        return selectedItems.size == 1
    }

    fun isSolidChunk() : Boolean {
        val positions = selectedPositions
        if (positions.isEmpty()) {
            return false
        }
        var previousPosition = positions[0]

        return positions.subList(1, positions.size).all {
            val status = (it - previousPosition) == 1
            previousPosition = it
            return status
        }
    }

    fun canMoveUp(): Boolean {
        val positions = selectedPositions
        return isSolidChunk() && positions[0] > 0
    }

    fun canMoveDown(): Boolean {
        val positions = selectedPositions
        return isSolidChunk() && positions[positions.size - 1] < items.size - 1
    }

    fun canDelete(): Boolean {
        return !selectedItems.isEmpty()
    }

    fun canSelectAll(): Boolean {
        return selectedItems.size < items.size
    }

    private fun cleanSelection() {
        selectedItems = selectedItems.filter {items.contains(it)}.toMutableSet()
    }

    private fun changed() {
        cleanSelection()
        handleStateChange(selectableItems)
    }

    fun itemClick(item: ItemType) {
        if (selectedItems.size == 0) {
            return
        }
        if (selectedItems.contains(item)) {
            selectedItems.remove(item)
        } else {
            selectedItems.add(item)
        }
        changed()
    }

    fun itemLongClick(item: ItemType) {
        if (selectedItems.size == 0) {
            selectedItems.add(item)
        }
        changed()
    }

    fun setItemConsumer(itemConsumer: (ItemType) -> Unit) {
        this.itemConsumer = itemConsumer
    }

    fun unselectAll() {
        this.selectedItems.clear()
        changed()
    }

    fun setItems(items: List<ItemType>) {
        this.items = kryo.copy(items.toMutableList())
        changed()
    }

    private fun dataSetChanged() {
        changed()
        handleCollectionChange(items)
    }

    fun deleteSelected() {
        items = items.filter { !selectedItems.contains(it) }.toMutableList()
        dataSetChanged()
    }

    fun moveSelectionUp() {
        if (!canMoveUp()) {
            return
        }
        val positions = selectedPositions

        val positionToMoveDown = positions[0] - 1
        val itemToMoveDown = items[positionToMoveDown]

        items.add(positions[positions.size - 1] + 1, itemToMoveDown)
        items.removeAt(positionToMoveDown)

        dataSetChanged()
    }

    fun moveSelectionDown() {
        if (!canMoveDown()) {
            return
        }
        val positions = selectedPositions

        val positionToMoveUp = positions[positions.size - 1] + 1
        val itemToMoveUp = items[positionToMoveUp]

        items.removeAt(positionToMoveUp)
        items.add(positions[0], itemToMoveUp)

        dataSetChanged()
    }

    fun selectAll() {
        selectedItems.addAll(items)
        changed()
    }

    fun triggerConsumption() {
        if (itemConsumer == null) {
            return
        }

        val iterator = selectedItems.iterator()
        if (iterator.hasNext()) {
            itemConsumer?.invoke(kryo.copy(iterator.next()))
        }
    }

    fun saveState(): Serializable {
        return HashSet(selectedItems)
    }

    fun loadState(state: Serializable) {
        selectedItems = state as HashSet<ItemType>
        changed()
    }

}
