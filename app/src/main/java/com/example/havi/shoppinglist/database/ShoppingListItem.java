package com.example.havi.shoppinglist.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "shoppinglistitem")
    public class ShoppingListItem implements Serializable {
        @ColumnInfo(name = "id")
        @PrimaryKey(autoGenerate = true)
        public Long id;

        @ColumnInfo(name = "name")
        public String name;

        @ColumnInfo(name = "is_bought")
        public boolean isBought;

        @ColumnInfo(name = "shopping_items")
        List<ShoppingItem> shoppingItems;
}
