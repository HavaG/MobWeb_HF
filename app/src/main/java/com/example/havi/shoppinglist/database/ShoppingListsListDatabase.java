package com.example.havi.shoppinglist.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(
            entities = {ShoppingListItem.class, ShoppingItem.class, Category.class},
            version = 3
    )
    @TypeConverters({Converters.class})
    public abstract class ShoppingListsListDatabase extends RoomDatabase {
    public abstract ShoppingListItemDao shoppingListItemDao();
    public abstract ShoppingItemDao shoppingItemDao();
    public abstract CategoryDao categoryDao();
    private static ShoppingListsListDatabase INSTANCE;

    public synchronized static ShoppingListsListDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context,
                    ShoppingListsListDatabase.class,
                    "shopping-lists")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}