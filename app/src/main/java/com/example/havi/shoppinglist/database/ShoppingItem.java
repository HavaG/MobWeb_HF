package com.example.havi.shoppinglist.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "shoppingitem")
    public class ShoppingItem {
        @ColumnInfo(name = "id")
        @PrimaryKey(autoGenerate = true)
        public Long id;

        @ColumnInfo(name = "name")
        public String name;

        @ColumnInfo(name = "category")
        public Category category;

        @ColumnInfo(name = "is_bought")
        public boolean isBought;

        @ColumnInfo(name = "list_id")
        public Long list_id;
    }