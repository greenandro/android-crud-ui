package com.gurunars.item_list;

import java.io.Serializable;

/**
 * Abstraction of the entity that can be shown in the ItemListView.
 *
 * "equals" method MUST be implemented to compare items subclasses by value.
 */
public abstract class Item<PayloadType extends Serializable> implements Serializable {

    private final Enum type;
    private final long id;
    private PayloadType payload;

    public Item(Enum type, long id, PayloadType payload) {
        this.type = type;
        this.id = id;
        this.payload = payload;
    }

    /**
     * @return value to be used when deciding which view to use to render the items in a
     *         RecyclerView
     */
    public final Enum getType() {
        return type;
    }

    /**
     * @return value to differentiate one item from another within a RecyclerView
     */
    public final long getId() {
        return id;
    }

    public final PayloadType getPayload() {
        return payload;
    }

    public final void setPayload(PayloadType payload) {
        this.payload = payload;
    }

    @Override
    public final boolean equals(Object other) {
        return
            other != null &&
            this.getClass() == other.getClass() &&
            getId() == ((Item) other).getId();
    }

    @Override
    public final int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }
}
