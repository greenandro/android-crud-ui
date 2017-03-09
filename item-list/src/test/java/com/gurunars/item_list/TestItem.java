package com.gurunars.item_list;

class TestItem extends Item<Integer> {

    TestItem(int id, Integer payload) {
        super(null, id, payload);
    }

    @Override
    public String toString() {
        return "new TestItem(" + getId() + ", " + getPayload() + ")";
    }

}
