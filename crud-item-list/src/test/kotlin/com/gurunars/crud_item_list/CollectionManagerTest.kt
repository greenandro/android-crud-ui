package com.gurunars.crud_item_list

import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.internal.util.collections.Sets
import java.util.*


abstract class BaseCollectionManagerTest {

    lateinit internal var collectionManager: CollectionManager<AnimalItem>
    lateinit internal var selectableItems: List<SelectableItem<AnimalItem>>
    lateinit internal var items: MutableList<AnimalItem>

    internal fun assertStatus(
            itemsSelected: Boolean = true,
            canEdit: Boolean = false,
            canMoveUp: Boolean = true,
            canMoveDown: Boolean = true,
            canDelete: Boolean = true,
            canSelectAll: Boolean = true
    ) {
        assertEquals(canDelete, collectionManager.canDelete())
        assertEquals(canEdit, collectionManager.canEdit())
        assertEquals(canMoveDown, collectionManager.canMoveDown())
        assertEquals(canMoveUp, collectionManager.canMoveUp())
        assertEquals(canSelectAll, collectionManager.canSelectAll())
        assertEquals(itemsSelected, collectionManager.hasSelection())
    }

    @Before
    fun before() {
        collectionManager = CollectionManager(
            {
                this@BaseCollectionManagerTest.selectableItems = it
            }, { items = it }
        )

        items = Arrays.asList(
            AnimalItem(1, AnimalItem.Type.MONKEY),
            AnimalItem(2, AnimalItem.Type.TIGER),
            AnimalItem(3, AnimalItem.Type.WOLF),
            AnimalItem(4, AnimalItem.Type.LION),
            AnimalItem(5, AnimalItem.Type.MONKEY),
            AnimalItem(6, AnimalItem.Type.TIGER),
            AnimalItem(7, AnimalItem.Type.WOLF),
            AnimalItem(8, AnimalItem.Type.LION)
        )

        collectionManager.setItems(items)
    }

}


class ItemTest {

    @Test
    fun containmentInList_shouldWorkWithEqualsMethod() {
        val itemList = Arrays.asList(
            AnimalItem(1, AnimalItem.Type.MONKEY),
            AnimalItem(2, AnimalItem.Type.TIGER)
        )
        assertTrue(itemList.contains(
            AnimalItem(1, AnimalItem.Type.MONKEY)))
    }

    @Test
    fun containmentInSet_shouldWorkWithEqualsMethod() {
        val itemList = Sets.newSet(
            AnimalItem(1, AnimalItem.Type.MONKEY),
            AnimalItem(2, AnimalItem.Type.TIGER)
        )
        assertTrue(itemList.contains(
            AnimalItem(1, AnimalItem.Type.MONKEY)))
    }

}

class ClickTests : BaseCollectionManagerTest() {

    @Test
    fun shortClickWithoutSelection_noAction() {
        collectionManager.itemClick(items[0])
        assertFalse(selectableItems[0].isSelected)
        assertStatus(
            canDelete = false,
            canMoveUp = false,
            canMoveDown = false,
            itemsSelected = false
        )
    }

    @Test
    fun doubleLongClick_noExtraAction() {
        collectionManager.itemLongClick(items[1])
        collectionManager.itemLongClick(items[1])
        collectionManager.itemLongClick(items[0])
        assertTrue(selectableItems[1].isSelected)
        assertFalse(selectableItems[0].isSelected)
        assertStatus(
            canEdit = true
        )
    }

    @Test
    fun shortClickOnUnselectedAfterLongClick_shouldSelectAdditional() {
        collectionManager.itemLongClick(items[1])
        collectionManager.itemClick(items[2])

        assertTrue(selectableItems[1].isSelected)
        assertTrue(selectableItems[2].isSelected)

        assertStatus()
    }

    @Test
    fun shortClickOnSelection_shouldUnselect() {
        collectionManager.itemLongClick(items[0])
        collectionManager.itemClick(items[0])
        assertFalse(selectableItems[0].isSelected)

        assertStatus(
            canDelete = false,
            canMoveUp = false,
            canMoveDown = false,
            itemsSelected = false
        )
    }

}

class SelectionTests : BaseCollectionManagerTest() {

    @Test
    fun unselectingSelectedItems_shouldUnselect() {
        collectionManager.itemLongClick(items[0])
        collectionManager.itemClick(items[1])

        collectionManager.unselectAll()

        assertFalse(selectableItems[0].isSelected)
        assertFalse(selectableItems[1].isSelected)

        assertStatus(
            canDelete = false,
            canMoveUp = false,
            canMoveDown = false,
            itemsSelected = false
        )
    }

    @Test
    fun selectAll_shouldSelectAll() {
        collectionManager.selectAll()

        for (item in selectableItems) {
            assertTrue(item.isSelected)
        }

        assertStatus(
            canMoveUp = false,
            canMoveDown = false,
            canSelectAll = false
        )
    }

    @Test
    fun selectTopMostOne_shouldDisableMoveUp() {
        collectionManager.itemLongClick(items[0])
        assertStatus(
            canMoveUp = false,
            canDelete = true,
            canEdit = true,
            canMoveDown = true,
            canSelectAll = true
        )
    }

    @Test
    fun selectBottomMostOne_shouldDisableMoveDown() {
        collectionManager.itemLongClick(items[items.size - 1])
        assertStatus(
            canMoveUp = true,
            canDelete = true,
            canEdit = true,
            canMoveDown = false,
            canSelectAll = true
        )
    }

    @Test
    fun selectMultiple_shouldDisableEditing() {
        collectionManager.itemLongClick(items[1])
        collectionManager.itemClick(items[2])
        assertStatus(
            canMoveUp = true,
            canDelete = true,
            canEdit = false,
            canMoveDown = true,
            canSelectAll = true
        )
    }

    @Test
    fun selectChunkBeforeTheLastOne_shouldEnableMoveDown() {
        collectionManager.selectAll()
        collectionManager.itemClick(items[items.size - 1])
        assertStatus(
            canMoveUp = false,
            canDelete = true,
            canEdit = false,
            canMoveDown = true,
            canSelectAll = true
        )
    }

    @Test
    fun selectChunkAfterTheFirstOne_shouldDisableMoveUp() {
        collectionManager.selectAll()
        collectionManager.itemClick(items[0])
        assertStatus(
            canMoveUp = true,
            canDelete = true,
            canEdit = false,
            canMoveDown = false,
            canSelectAll = true
        )
    }

    @Test
    fun selectNonSolidChunk_shouldDisableMoves() {
        collectionManager.itemLongClick(items[1])
        collectionManager.itemClick(items[3])
        assertStatus(
            canMoveUp = false,
            canDelete = true,
            canEdit = false,
            canMoveDown = false,
            canSelectAll = true
        )
    }

    @Test
    fun selectSolidChunk_shouldEnableMoves() {
        collectionManager.itemLongClick(items[1])
        collectionManager.itemClick(items[2])
        collectionManager.itemClick(items[3])
        assertStatus(
            canMoveUp = true,
            canDelete = true,
            canEdit = false,
            canMoveDown = true,
            canSelectAll = true
        )
    }

}


class MoveDownTests : BaseCollectionManagerTest() {

    @Test
    fun moveTopmostDown_shouldEnableMoveUp() {
        collectionManager.itemLongClick(items[0])

        assertStatus(
            canEdit = true,
            canMoveUp = false
        )

        collectionManager.moveSelectionDown()

        assertEquals(items[0].id, 2)
        assertEquals(items[1].id, 1)
        assertEquals(items[2].id, 3)

        assertStatus(
            canEdit = true,
            canMoveUp = true
        )
    }

    @Test
    fun move_shouldWorkWell() {
        collectionManager.itemLongClick(items[2])
        collectionManager.itemClick(items[3])
        collectionManager.itemClick(items[4])
        collectionManager.itemClick(items[5])
        collectionManager.moveSelectionDown()
        assertEquals(
            listOf(
                AnimalItem(1, AnimalItem.Type.MONKEY),
                AnimalItem(2, AnimalItem.Type.TIGER),
                AnimalItem(7, AnimalItem.Type.WOLF),
                AnimalItem(3, AnimalItem.Type.WOLF),
                AnimalItem(4, AnimalItem.Type.LION),
                AnimalItem(5, AnimalItem.Type.MONKEY),
                AnimalItem(6, AnimalItem.Type.TIGER),
                AnimalItem(8, AnimalItem.Type.LION)
            ), items
        )
    }

    @Test
    fun moveDownWithNoSelection_noError() {
        collectionManager.moveSelectionDown()
    }

}

class MoveUpTests : BaseCollectionManagerTest() {

    @Test
    fun moveBottommostUp_shouldEnableMoveDown() {
        collectionManager.itemLongClick(items[items.size - 1])

        assertStatus(
            canEdit = true,
            canMoveDown = false
        )

        collectionManager.moveSelectionUp()

        assertEquals(items[items.size - 1].id, 7)
        assertEquals(items[items.size - 2].id, 8)
        assertEquals(items[items.size - 3].id, 6)

        assertStatus(
            canEdit = true,
            canMoveDown = true
        )
    }

    @Test
    fun move_shouldWorkWell() {
        collectionManager.itemLongClick(items[2])
        collectionManager.itemClick(items[3])
        collectionManager.itemClick(items[4])
        collectionManager.itemClick(items[5])
        collectionManager.moveSelectionUp()
        assertEquals(
            listOf(
                AnimalItem(1, AnimalItem.Type.MONKEY),
                AnimalItem(3, AnimalItem.Type.WOLF),
                AnimalItem(4, AnimalItem.Type.LION),
                AnimalItem(5, AnimalItem.Type.MONKEY),
                AnimalItem(6, AnimalItem.Type.TIGER),
                AnimalItem(2, AnimalItem.Type.TIGER),
                AnimalItem(7, AnimalItem.Type.WOLF),
                AnimalItem(8, AnimalItem.Type.LION)
            ), items
        )
    }

    @Test
    fun moveUpWithNoSelection_noError() {
        collectionManager.moveSelectionUp()
    }

}

class DeletionTests : BaseCollectionManagerTest() {

    @Test
    fun deleteAll_shouldDisableAllActions() {
        collectionManager.selectAll()
        collectionManager.deleteSelected()

        assertEquals(ArrayList<Any>(), selectableItems)

        assertStatus(
            canDelete = false,
            canMoveUp = false,
            canMoveDown = false,
            itemsSelected = false,
            canSelectAll = false
        )
    }

    @Test
    fun deleteWithNoSelection_noError() {
        collectionManager.deleteSelected()
    }

}


class ResettingTest : BaseCollectionManagerTest() {

    @Test
    fun resettingItems_shouldRetainSelection() {
        collectionManager.itemLongClick(items[1])
        collectionManager.setItems(items)

        assertTrue(selectableItems[1].isSelected)

        assertStatus(
            canEdit = true
        )
    }

    @Test
    fun resettingItemsWithSelectionRemoved_shouldCancelSelection() {
        items = ArrayList(items)

        collectionManager.itemLongClick(items[1])
        items.removeAt(1)
        collectionManager.setItems(items)

        assertStatus(
            canDelete = false,
            canMoveUp = false,
            canMoveDown = false,
            itemsSelected = false
        )
    }

}

class EditTests : BaseCollectionManagerTest() {

    @Test
    fun triggerConsumption() {
        collectionManager.triggerConsumption()

        var editedItem: AnimalItem? = null

        assertEquals(null, editedItem)

        collectionManager.setItemConsumer({ animalItem -> editedItem = animalItem })

        collectionManager.triggerConsumption()

        assertEquals(null, editedItem)

        collectionManager.itemLongClick(items[1])
        collectionManager.triggerConsumption()

        assertEquals(AnimalItem(2, AnimalItem.Type.TIGER), editedItem)
    }

}


class StateTests : BaseCollectionManagerTest() {

    @Test
    fun stateSaveAndLoad() {
        collectionManager.itemLongClick(items[1])

        val payload = collectionManager.saveState()
        collectionManager.itemClick(items[1])

        assertFalse(selectableItems[1].isSelected)

        collectionManager.loadState(payload)

        assertTrue(selectableItems[1].isSelected)

        assertStatus(
            canEdit = true
        )
    }

}
