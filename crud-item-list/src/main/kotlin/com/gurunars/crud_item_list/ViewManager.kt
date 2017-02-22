package com.gurunars.crud_item_list

import android.view.ViewManager
import com.gurunars.crud_item_list.ContextualMenu
import com.gurunars.crud_item_list.CrudItemList
import com.gurunars.item_list.Item
import org.jetbrains.anko.custom.ankoView

inline fun <ItemT: Item> ViewManager.crudItemList(theme: Int = 0) : CrudItemList<ItemT> = crudItemList(theme) {}
inline fun <ItemT: Item> ViewManager.crudItemList(theme: Int = 0, init: CrudItemList<ItemT>.() -> Unit) = ankoView({ CrudItemList(it) }, theme, init)

internal inline fun ViewManager.contextualMenu(theme: Int = 0) = contextualMenu(theme) {}
internal inline fun ViewManager.contextualMenu(theme: Int = 0, init: ContextualMenu.() -> Unit) = ankoView({ ContextualMenu(it) }, theme, init)

internal inline fun ViewManager.circleButton(theme: Int = 0) = circleButton(theme) {}
internal inline fun ViewManager.circleButton(theme: Int = 0, init: CircularIconButton.() -> Unit) = ankoView({ CircularIconButton(it) }, theme, init)
