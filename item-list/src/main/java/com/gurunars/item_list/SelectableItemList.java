package com.gurunars.item_list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.io.Serializable;

public class SelectableItemList<PayloadType extends Serializable> extends FrameLayout {

    public SelectableItemList(Context context) {
        this(context, null);
    }

    public SelectableItemList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectableItemList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.selectable_item_list, this);
    }
}
