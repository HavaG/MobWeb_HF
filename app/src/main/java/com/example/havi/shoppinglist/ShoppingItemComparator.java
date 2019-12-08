package com.example.havi.shoppinglist;

import com.example.havi.shoppinglist.database.ShoppingItem;

import java.util.Comparator;

public class ShoppingItemComparator implements Comparator<ShoppingItem> {

    @Override
    public int compare(ShoppingItem s1, ShoppingItem s2) {
        return s1.category.name.compareTo(s2.category.name);
    }
}