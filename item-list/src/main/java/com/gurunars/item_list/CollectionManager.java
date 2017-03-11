package com.gurunars.item_list;

import com.esotericsoftware.kryo.Kryo;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java8.util.function.Consumer;


class CollectionManager<PayloadType extends Payload> implements Serializable {

    private Kryo kryo = new Kryo();

    private List<Item<PayloadType>> items = new ArrayList<>();
    private Set<Item<PayloadType>> selectedItems = new HashSet<>();

    private final Consumer<List<Item<SelectablePayload<PayloadType>>>> stateChangeHandler;

    CollectionManager(Consumer<List<Item<SelectablePayload<PayloadType>>>> stateChangeHandler) {
        this.stateChangeHandler = stateChangeHandler;
        this.kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
    }

    private void changed() {
        Set<Item<PayloadType>> newSelection = new HashSet<>(selectedItems);
        for (Item<PayloadType> cursor: selectedItems) {
            newSelection.remove(cursor);
            if (items.contains(cursor)) {
                // This is to replace the item's payload with a new one
                newSelection.add(items.get(items.indexOf(cursor)));
            }
        }
        selectedItems = newSelection;
        List<Item<SelectablePayload<PayloadType>>> selectableItems = new ArrayList<>();
        for (Item<PayloadType> item: items) {
            selectableItems.add(new Item<>(item.getId(),
                    new SelectablePayload<>(item.getPayload(), selectedItems.contains(item))));
        }
        stateChangeHandler.accept(selectableItems);
    }

    void itemClick(Item<PayloadType> item) {
        if (selectedItems.size() == 0) {
            return;
        }

        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
        changed();
    }

    void itemLongClick(Item<PayloadType> item) {
        if (selectedItems.size() == 0) {
            selectedItems.add(item);
        }
        changed();
    }

    void setItems(List<Item<PayloadType>> items) {
        this.items = kryo.copy(new ArrayList<>(items));
        changed();
    }

    void setSelectedItems(Set<Item<PayloadType>> selectedItems) {
        this.selectedItems = kryo.copy(new HashSet<>(selectedItems));
        changed();
    }

    Set<Item<PayloadType>> getSelectedItems() {
        return kryo.copy(selectedItems);
    }

}
