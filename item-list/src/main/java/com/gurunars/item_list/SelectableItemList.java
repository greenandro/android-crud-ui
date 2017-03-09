package com.gurunars.item_list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import butterknife.ButterKnife;

public class SelectableItemList<PayloadType extends Payload> extends FrameLayout {

    private ItemList<ConcreteSelectablePayload<PayloadType>> itemList;

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
}
