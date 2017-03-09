package com.gurunars.item_list;

import java.io.Serializable;

final class ConcreteSelectablePayload<PayloadType extends Payload> implements Payload {

    private final PayloadType payload;
    private boolean selected = false;

    public ConcreteSelectablePayload(PayloadType payload) {
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
        if (obj instanceof ConcreteSelectablePayload) {
            ConcreteSelectablePayload other = (ConcreteSelectablePayload) obj;
            return payload.equals(other.payload) && selected == other.selected;
        }
        return false;
    }

    @Override
    public Enum getType() {
        return payload.getType();
    }
}
