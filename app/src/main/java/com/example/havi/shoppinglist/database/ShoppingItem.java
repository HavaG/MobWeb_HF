package com.example.havi.shoppinglist.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "shoppingitem")
    public class ShoppingItem implements Serializable {
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