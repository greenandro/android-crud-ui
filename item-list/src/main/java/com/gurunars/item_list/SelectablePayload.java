package com.gurunars.item_list;

import java.io.Serializable;

public final class SelectablePayload<PayloadType extends Serializable> {

    private final PayloadType payload;
    private boolean selected = false;

    public SelectablePayload(PayloadType payload) {
        this.payload = payload;
    }

    public final boolean isSelected() {
        return selected;
    }

    public final void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int hashCode() {
        return payload.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SelectablePayload) {
            SelectablePayload other = (SelectablePayload) obj;
            return payload.equals(other.payload) && selected == other.selected;
        }
        return false;
    }
}
