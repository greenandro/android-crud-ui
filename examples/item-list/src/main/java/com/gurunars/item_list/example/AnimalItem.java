package com.gurunars.item_list.example;


import com.gurunars.item_list.Item;

class AnimalItem extends Item<Integer> {

    enum Type {
        MONKEY, TIGER, WOLF, LION
    }

    public void update() {
        this.setPayload(getPayload()+1);
    }

    public AnimalItem(long id, Type type) {
        super(type, id, 0);
    }

    @Override
    public String toString() {
        return "" + getId() + " @ " + getPayload();
    }
}
