package com.gurunars.crud_item_list;

import android.support.annotation.IdRes;
import android.view.View;

import com.esotericsoftware.kryo.Kryo;
import com.gurunars.item_list.Item;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import java8.util.function.Consumer;


class CollectionManager<ItemType extends Item> implements Serializable {

    private Kryo kryo = new Kryo();

    private List<ItemHolder<ItemType>> items = new ArrayList<>();
    private Set<ItemHolder<ItemType>> selectedItems = new HashSet<>();

    private final Consumer<List<SelectableItem<ItemType>>> stateChangeHandler;
    private final Consumer<List<ItemType>> collectionChangeHandler;
    private View contextualMenu;

    private final ActionEdit<ItemHolder<ItemType>> actionEdit = new ActionEdit<>();

    private final Map<View, Action<ItemHolder<ItemType>>> actions = new HashMap<>();

    CollectionManager(
            View contextualMenu,
            Consumer<List<SelectableItem<ItemType>>> stateChangeHandler,
            Consumer<List<ItemType>> collectionChangeHandler) {
        this.contextualMenu = contextualMenu;
        this.stateChangeHandler = stateChangeHandler;
        this.collectionChangeHandler = collectionChangeHandler;
        this.kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));

        registerAction(R.id.selectAll, new ActionSelectAll<ItemHolder<ItemType>>());
        registerAction(R.id.delete, new ActionDelete<ItemHolder<ItemType>>());
        registerAction(R.id.edit, actionEdit);
        registerAction(R.id.moveUp, new ActionMoveUp<ItemHolder<ItemType>>());
        registerAction(R.id.moveDown, new ActionMoveDown<ItemHolder<ItemType>>());
    }

    private List<SelectableItem<ItemType>> getSelectableItems() {
        List<SelectableItem<ItemType>> rval = new ArrayList<>();
        for (ItemHolder<ItemType> item: items) {
            rval.add(new ConcreteSelectableItem<>(item.getRaw(), selectedItems.contains(item)));
        }
        return rval;
    }

    void registerAction(@IdRes int itemId, final Action<ItemHolder<ItemType>> action) {
        View itemView = ButterKnife.findById(contextualMenu, itemId);
        this.actions.put(itemView, action);
        itemView.setEnabled(action.canPerform(items, selectedItems));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!action.canPerform(items, selectedItems)) {
                    return;
                }
                action.perform(items, selectedItems);
                changed();
                collectionChangeHandler.accept(ItemHolder.unwrap(items));
                v.setEnabled(action.canPerform(items, selectedItems));
            }
        });
    }

    boolean hasSelection() {
        return !selectedItems.isEmpty();
    }

    private void cleanSelection() {
        Set<ItemHolder<ItemType>> newSelection = new HashSet<>(selectedItems);
        for (ItemHolder<ItemType> cursor: selectedItems) {
            newSelection.remove(cursor);
            if (items.contains(cursor)) {
                // This is to replace the item's payload with a new one
                newSelection.add(items.get(items.indexOf(cursor)));
            }
        }
        selectedItems = newSelection;
    }

    private void changed() {
        cleanSelection();
        stateChangeHandler.accept(getSelectableItems());
        for (Map.Entry<View, Action<ItemHolder<ItemType>>> entry: actions.entrySet()) {
            entry.getKey().setEnabled(entry.getValue().canPerform(items, selectedItems));
        }
    }

    void itemClick(ItemType item) {
        if (selectedItems.size() == 0) {
            return;
        }

        ItemHolder<ItemType> holder = new ItemHolder<>(item);

        if (selectedItems.contains(holder)) {
            selectedItems.remove(holder);
        } else {
            selectedItems.add(holder);
        }
        changed();
    }

    void itemLongClick(ItemType item) {
        if (selectedItems.size() == 0) {
            selectedItems.add(new ItemHolder<>(item));
        }
        changed();
    }

    void setItemConsumer(final Consumer<ItemType> itemConsumer) {
        this.actionEdit.setItemConsumer(new Consumer<ItemHolder<ItemType>>() {
            @Override
            public void accept(ItemHolder<ItemType> holder) {
                itemConsumer.accept(holder.getRaw());
            }
        });
    }

    void unselectAll() {
        this.selectedItems.clear();
        changed();
    }

    void setItems(List<ItemType> items) {
        this.items = kryo.copy(new ArrayList<>(ItemHolder.wrap(items)));
        changed();
    }

    Serializable saveState() {
        return new HashSet<>(selectedItems);
    }

    void loadState(Serializable state) {
        selectedItems = (HashSet<ItemHolder<ItemType>>) state;
        changed();
    }

}
