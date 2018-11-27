package com.example.havi.shoppinglist.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

    @Database(
            entities = {ShoppingListItem.class, ShoppingItem.class},
            version = 1
    )
    @TypeConverters({Converters.class})
    public abstract class ShoppingListsListDatabase extends RoomDatabase {
        public abstract ShoppingListItemDao shoppingListItemDao();
        public abstract ShoppingItemDao shoppingItemDao();
    }