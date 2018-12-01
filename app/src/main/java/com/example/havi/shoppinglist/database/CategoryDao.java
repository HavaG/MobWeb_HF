package com.example.havi.shoppinglist.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categoryitem")
    List<Category> getAll();

    @Query("SELECT * FROM categoryitem WHERE id = :id LIMIT 1")
    Category get(long id);

    @Insert
    long insert(Category categoryItems);

    @Update
    void update(Category categoryItem);

    @Delete
    void deleteItem(Category categoryItem);
}