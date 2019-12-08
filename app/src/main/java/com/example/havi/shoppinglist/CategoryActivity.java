package com.example.havi.shoppinglist;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.havi.shoppinglist.Adapter.CategoryAdapter;
import com.example.havi.shoppinglist.Adapter.ItemAdapter;
import com.example.havi.shoppinglist.database.Category;
import com.example.havi.shoppinglist.database.ShoppingItem;
import com.example.havi.shoppinglist.database.ShoppingListsListDatabase;
import com.example.havi.shoppinglist.fragments.NewCategoryDialogFragment;
import com.example.havi.shoppinglist.fragments.NewShoppingItemDialogFragment;
import com.example.havi.shoppinglist.fragments.UpdateCategoryDialogFragment;
import com.example.havi.shoppinglist.fragments.UpdateShoppingItemDialogFragment;
import com.google.gson.Gson;

import java.util.List;

public class CategoryActivity extends AppCompatActivity
        implements NewCategoryDialogFragment.NewCategoryDialogListener,
        UpdateCategoryDialogFragment.UpdateCategoryDialogListener,
        CategoryAdapter.CategoryClickListener {

    private ShoppingListsListDatabase database;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = ShoppingListsListDatabase.getInstance(getBaseContext());
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Categories");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewCategoryDialogFragment().show(getSupportFragmentManager(), NewCategoryDialogFragment.TAG);
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        categoryRecyclerView = findViewById(R.id.ListRecyclerView);
        adapter = new CategoryAdapter(this);
        loadItemsInBackground();
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryRecyclerView.setAdapter(adapter);
    }


    @SuppressLint("StaticFieldLeak")
    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Category>>() {

            @Override
            protected List<Category> doInBackground(Void... voids) {
                return database.categoryDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Category> categoryItem) {
                adapter.update(categoryItem);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCategoryCreated(final Category newItem) {
        new AsyncTask<Void, Void, Category>() {

            @Override
            protected Category doInBackground(Void... voids) {
                newItem.id = database.categoryDao().insert(newItem);
                return newItem;
            }

            @Override
            protected void onPostExecute(Category categoryItem) {
                adapter.addItem(categoryItem);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onItemDeleted(final Category newItem) {
        new AsyncTask<Void, Void, Category>() {

            @Override
            protected Category doInBackground(Void... voids) {
                database.categoryDao().deleteItem(newItem);
                return null;
            }

            @Override
            protected void onPostExecute(Category categoryItem) {
                adapter.deleteItem(categoryItem);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onItemChanged(final Category categoryItem) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.categoryDao().update(categoryItem);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                adapter.updateItem(categoryItem);
            }
        }.execute();
    }
}
