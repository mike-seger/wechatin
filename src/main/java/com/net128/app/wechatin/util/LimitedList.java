package com.net128.app.wechatin.util;

import java.util.ArrayList;

public class LimitedList<E> extends ArrayList<E> {

    private int limit;

    public LimitedList(int limit) {
        this.limit = Math.max(limit, 1);
    }

    @Override
    public boolean add(E o) {
        boolean added = super.add(o);
        while (added && size() > limit) {
           super.remove(0);
        }
        return added;
    }
}