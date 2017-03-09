package com.gurunars.item_list;

import java.io.Serializable;

public interface Payload extends Serializable {
    /**
     * @return value to be used when deciding which view to use to render the items in a
     *         RecyclerView
     */
    Enum getType();
}
