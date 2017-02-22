package com.gurunars.item_list

import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView


inline fun <ItemType : Item> ViewManager.itemList(theme: Int = 0) : ItemList<ItemType> = itemList(theme) {}
inline fun <ItemType : Item> ViewManager.itemList(theme: Int = 0, init: ItemList<ItemType>.() -> Unit) = ankoView({ ItemList(it) }, theme, init)
