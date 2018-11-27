package com.example.havi.shoppinglist.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ShoppingListItemDao {
    @Query("SELECT * FROM shoppinglistitem")
    List<ShoppingListItem> getAll();

    @Query("SELECT * FROM shoppinglistitem WHERE id = :id LIMIT 1")
    ShoppingListItem get(int id);

    @Insert
    long insert(ShoppingListItem shoppingListItems);

    @Update
    void update(ShoppingListItem shoppingListItems);

    @Update
    void update(ShoppingItem shoppingItems);

    @Delete
    void deleteItem(ShoppingListItem shoppingListItems);
}