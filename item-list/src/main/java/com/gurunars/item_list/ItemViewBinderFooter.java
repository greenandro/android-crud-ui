package com.gurunars.item_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

class ItemViewBinderFooter implements ItemViewBinder {

    static final int FOOTER_TYPE = -42;

    @Override
    public View getView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.footer, null);
        view.setTag(FOOTER_TYPE, FOOTER_TYPE);
        return view;
    }

    @Override
    public void bind(View itemView, Item item, Item previousItem) {

    }
}
