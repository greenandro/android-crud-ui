package com.gurunars.item_list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;

public class SelectableItemList<PayloadType extends Payload> extends FrameLayout {

    private ItemList<SelectablePayload<PayloadType>> itemList;

    private List<Item<PayloadType>> items = new ArrayList<>();
    private Set<Item<PayloadType>> selectedItems = new HashSet<>();

    public SelectableItemList(Context context) {
        this(context, null);
    }

    public SelectableItemList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectableItemList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.selectable_item_list, this);
        itemList = ButterKnife.findById(this, R.id.itemList);


    }

    private void updateItemList() {
        List<Item<SelectablePayload<PayloadType>>> selectableItems = new ArrayList<>();
        for (Item<PayloadType> item: items) {
            selectableItems.add(new Item<>(item.getId(), new SelectablePayload<>(item.getPayload())));
        }
        itemList.setItems(selectableItems);
    }

    public void setItems(List<Item<PayloadType>> items) {
        this.items = items;
        updateItemList();
    }

    public void setSelection(Set<Item<PayloadType>> selectedItems) {
        this.selectedItems = selectedItems;
        updateItemList();
    }

    public void registerItemViewBinder(Enum itemType,
                                       ItemViewBinder<Item<SelectablePayload<PayloadType>>> itemViewBinder) {
        itemList.registerItemViewBinder(itemType, itemViewBinder);
    }

    public void setEmptyViewBinder(EmptyViewBinder emptyViewBinder) {
        itemList.setEmptyViewBinder(emptyViewBinder);
    }
}
