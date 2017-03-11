package com.gurunars.item_list;

final class SelectablePayload<PayloadType extends Payload> implements Payload {

    private final PayloadType payload;
    private boolean selected;

    SelectablePayload(PayloadType payload, boolean selected) {
        this.payload = payload;
        this.selected = selected;
    }

    public final boolean isSelected() {
        return selected;
    }

    public PayloadType getPayload() {
        return payload;
    }

    void setSelected(boolean selected) {
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

    @Override
    public Enum getType() {
        return payload.getType();
    }
}
