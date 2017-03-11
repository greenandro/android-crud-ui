package com.gurunars.item_list;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class SelectableItemList<PayloadType extends Payload> extends FrameLayout {

    private ItemList<SelectablePayload<PayloadType>> itemList;
    private CollectionManager<PayloadType> collectionManager;

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

        collectionManager = new CollectionManager<>(new Consumer<List<Item<SelectablePayload<PayloadType>>>>() {
            @Override
            public void accept(List<Item<SelectablePayload<PayloadType>>> items) {
                itemList.setItems(items);
            }
        });

    }

    public void setItems(List<Item<PayloadType>> items) {
        collectionManager.setItems(items);
    }

    public void setSelection(Set<Item<PayloadType>> selectedItems) {
        collectionManager.setSelectedItems(selectedItems);
    }

    public void registerItemViewBinder(Enum itemType,
                                       ItemViewBinder<SelectablePayload<PayloadType>> itemViewBinder) {
        itemList.registerItemViewBinder(itemType, itemViewBinder);
    }

    public void setEmptyViewBinder(EmptyViewBinder emptyViewBinder) {
        itemList.setEmptyViewBinder(emptyViewBinder);
    }

    public Set<Item<PayloadType>> getSelectedItems() {
        return collectionManager.getSelectedItems();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putSerializable("selectedItems", new HashSet<>(collectionManager.getSelectedItems()));
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle localState = (Bundle) state;
        super.onRestoreInstanceState(localState.getParcelable("superState"));
        collectionManager.setSelectedItems((HashSet<Item<PayloadType>>) localState.getSerializable("selectedItems"));
    }
}
