package com.gurunars.crud_item_list

import com.gurunars.item_list.Item
import com.gurunars.item_list.ItemViewBinder

interface SelectableItemViewBinder<ItemType : Item> : ItemViewBinder<SelectableItem<ItemType>>
