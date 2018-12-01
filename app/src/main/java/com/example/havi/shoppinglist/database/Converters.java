package com.example.havi.shoppinglist.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Converters {
    @TypeConverter
    public static List<ShoppingItem> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<ShoppingItem> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static Category fromCategoryString(String value) {
        Type listType = new TypeToken<Category>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromCategory(Category category) {
        Gson gson = new Gson();
        String json = gson.toJson(category);
        return json;
    }
}