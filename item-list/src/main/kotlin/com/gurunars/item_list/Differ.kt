package com.gurunars.item_list


object Constants {
    val moveThreshold = 5
}


internal data class Range(val start: Int, val end: Int)


/*
* For two lists returns such a sublist that the missing parts represent the common portions
* to the left and to the right of both lists.
*
* E.g. for [1,2,3,4,5,6] and [1,3,4,5,2,6] returns [3,4,5,2]
*
* */
internal fun getPermutationRange(source: List<Any>, target: List<Any>): Range {
    var startOffset = 0
    var endOffset = 0

    val sourceSize = source.size
    val targetSize = target.size

    val limit = Math.min(sourceSize, targetSize)

    while (startOffset < limit) {
        if (source[startOffset] != target[startOffset]) {
            break
        }
        startOffset++
    }

    while (endOffset < limit) {
        if (source[sourceSize - 1 - endOffset] != target[targetSize - 1 - endOffset]) {
            break
        }
        endOffset++
    }

    return Range(startOffset, targetSize - endOffset)
}


internal fun <ItemType:Item> getComplexPermutation(
        source: List<ItemHolder<ItemType>>,
        target: List<ItemHolder<ItemType>>
): Change<ItemType> {
    val range = getPermutationRange(source, target)
    return ChangeComplexPermutation(
            range.start,
            ItemHolder.unwrap(target.subList(range.start, range.end)))
}


internal fun <ItemType:Item> getUnidirectionalPermutations(
        remainedOrderOneOriginal: List<ItemHolder<ItemType>>,
        remainedOrderTwo: List<ItemHolder<ItemType>>,
        reverse: Boolean): List<Change<ItemType>> {
    val changes = mutableListOf<ChangeMove<ItemType>>()
    val remainedOrderOne = remainedOrderOneOriginal.toMutableList()
    var from = if (reverse) remainedOrderOne.size - 1 else 0
    while (from < remainedOrderOne.size && from >= 0) {
        val item = remainedOrderOne[from]
        val to = remainedOrderTwo.indexOf(item)

        if (from != to) {
            changes.add(ChangeMove(item.raw, from, to))
            if (changes.size > Constants.moveThreshold) {
                return listOf(getComplexPermutation(
                        remainedOrderOneOriginal,
                        remainedOrderTwo))
            }
            remainedOrderOne.removeAt(from)
            remainedOrderOne.add(to, item)
            from = if (reverse) remainedOrderOne.size - 1 else 0
            continue
        }

        from += if (reverse) -1 else +1
    }

    return changes
}


internal fun <ItemType:Item> getPermutations(
        intersectionSourceOrder: List<ItemHolder<ItemType>>,
        intersectionTargetOrder: List<ItemHolder<ItemType>>): List<Change<ItemType>> {

    val moves = getUnidirectionalPermutations(
            intersectionSourceOrder, intersectionTargetOrder, false)
    val movesReverse = getUnidirectionalPermutations(
            intersectionSourceOrder, intersectionTargetOrder, true)

    if (moves.size == 1 && moves[0] is ChangeComplexPermutation<ItemType>) {
        return movesReverse
    } else {
        return if (movesReverse.size < moves.size) movesReverse else moves
    }
}


internal fun <ItemType:Item> getDiff(source: List<ItemType>, target: List<ItemType>): List<Change<ItemType>> {
    val sourceList = ItemHolder.wrap(source)
    val targetList = ItemHolder.wrap(target)

    // remove in a reverse order to prevent index recalculation
    val removed = sourceList.filter { !targetList.contains(it) }.reversed()

    val changes = mutableListOf<Change<ItemType>>()

    for (item in removed) {
        val position = sourceList.indexOf(item)
        changes.add(ChangeDelete(item.raw, position, position))
        sourceList.removeAt(position)
    }

    val added = targetList.filter { !sourceList.contains(it) }

    for (item in added) {
        val position = targetList.indexOf(item)
        changes.add(ChangeCreate(item.raw, position, position))
        sourceList.add(position, item)
    }

    val intersectionSourceOrder = sourceList.filter { targetList.contains(it) }
    val intersectionTargetOrder = targetList.filter { sourceList.contains(it) }

    changes.addAll(getPermutations(intersectionSourceOrder, intersectionTargetOrder))

    for (item in sourceList) {
        val index = targetList.indexOf(item)
        val newItem = targetList[index]
        if (item.raw != newItem.raw) {
            changes.add(ChangeUpdate(newItem.raw, index, index))
        }
    }

    return changes
}
