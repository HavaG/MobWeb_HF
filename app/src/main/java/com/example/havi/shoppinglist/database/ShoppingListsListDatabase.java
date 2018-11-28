package com.example.havi.shoppinglist.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(
            entities = {ShoppingListItem.class, ShoppingItem.class},
            version = 2
    )
    @TypeConverters({Converters.class})
    public abstract class ShoppingListsListDatabase extends RoomDatabase {
    public abstract ShoppingListItemDao shoppingListItemDao();
    public abstract ShoppingItemDao shoppingItemDao();





    public static final String DATABASE_NAME = "list-db";

    private static ShoppingListsListDatabase INSTANCE;

    public static ShoppingListsListDatabase getDatabase(Context context){

        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, ShoppingListsListDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}