package com.example.havi.shoppinglist;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.havi.shoppinglist.database.Category;
import com.example.havi.shoppinglist.database.ShoppingItem;
import com.example.havi.shoppinglist.database.ShoppingListsListDatabase;
import com.example.havi.shoppinglist.fragments.NewShoppingItemDialogFragment;
import com.example.havi.shoppinglist.Adapter.ItemAdapter;
import com.example.havi.shoppinglist.fragments.UpdateShoppingItemDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity
        implements NewShoppingItemDialogFragment.NewShoppingItemDialogListener, UpdateShoppingItemDialogFragment.UpdateShoppingItemDialogListener,
        ItemAdapter.ShoppingItemClickListener {

    private ShoppingListsListDatabase database;

    private RecyclerView listRecyclerView;
    private ItemAdapter adapter;

    private List<Category> categories;

    private long item_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = ShoppingListsListDatabase.getInstance(getBaseContext());
        loadBackground();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        item_id = intent.getLongExtra("item_id", -1);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("List");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewShoppingItemDialogFragment().show(getSupportFragmentManager(), NewShoppingItemDialogFragment.TAG);
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        listRecyclerView = findViewById(R.id.ListRecyclerView);
        adapter = new ItemAdapter(this);
        loadItemsInBackground();
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listRecyclerView.setAdapter(adapter);
    }


    @SuppressLint("StaticFieldLeak")
    private void loadBackground() {
        new AsyncTask<Void, Void, List<Category>>() {

            @Override
            protected List<Category> doInBackground(Void... voids) {
                categories = database.categoryDao().getAll();
                return database.categoryDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Category> categoryItems) {
                categories = categoryItems;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<ShoppingItem>>() {

            @Override
            protected List<ShoppingItem> doInBackground(Void... voids) {
                return database.shoppingItemDao().getAll(item_id);
            }

            @Override
            protected void onPostExecute(List<ShoppingItem> shoppingItems) {
                adapter.update(shoppingItems);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onItemChanged(final ShoppingItem listItem) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.shoppingItemDao().update(listItem);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                adapter.updateItem(listItem);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onShoppingItemCreated(final ShoppingItem newItem) {
        new AsyncTask<Void, Void, ShoppingItem>() {

            @Override
            protected ShoppingItem doInBackground(Void... voids) {
                newItem.list_id = item_id;
                long id = newItem.category.id;
                newItem.category = database.categoryDao().get(id);
                newItem.id = database.shoppingItemDao().insert(newItem);
                return newItem;
            }

            @Override
            protected void onPostExecute(ShoppingItem shoppingItem) {
                adapter.addItem(shoppingItem);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onItemDeleted(final ShoppingItem newItem) {
        new AsyncTask<Void, Void, ShoppingItem>() {

            @Override
            protected ShoppingItem doInBackground(Void... voids) {
                database.shoppingItemDao().deleteItem(newItem);
                return null;
            }

            @Override
            protected void onPostExecute(ShoppingItem shoppingItem) {
                adapter.deleteItem(shoppingItem);
            }
        }.execute();
    }

}