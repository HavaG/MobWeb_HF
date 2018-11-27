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

import com.example.havi.shoppinglist.database.ShoppingItem;
import com.example.havi.shoppinglist.database.ShoppingListItem;
import com.example.havi.shoppinglist.database.ShoppingListsListDatabase;
import com.example.havi.shoppinglist.fragments.NewShoppingItemDialogFragment;
import com.example.havi.shoppinglist.listAdapter.ItemAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity
        implements NewShoppingItemDialogFragment.NewShoppingItemDialogListener,
        ItemAdapter.ShoppingItemClickListener {

    private ShoppingListsListDatabase database;

    private RecyclerView listRecyclerView;
    private ItemAdapter adapter;

    private ShoppingListItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
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

        Intent intent = getIntent();
        String s = intent.getStringExtra("item");
        item = new Gson().fromJson(s, ShoppingListItem.class);

        database = Room.databaseBuilder(
                getApplicationContext(),
                ShoppingListsListDatabase.class,
                "shopping-lists"
        ).build();

        initRecyclerView();
    }

    private void initRecyclerView() {
        listRecyclerView = findViewById(R.id.ListRecyclerView);
        adapter = new ItemAdapter((ItemAdapter.ShoppingItemClickListener) this);
        loadItemsInBackground();
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listRecyclerView.setAdapter(adapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<ShoppingItem>>() {

            @Override
            protected List<ShoppingItem> doInBackground(Void... voids) {

                List<ShoppingListItem> index = database.shoppingListItemDao().getAll();
                ShoppingListItem temp = null;
                for(int i = 0; i < index.size(); i++){
                    if(index.get(i).id.equals(item.id))
                    {
                        temp = index.get(i);
                        break;
                    }
                }
                if(temp.shoppingItems == null)
                    temp.shoppingItems = new ArrayList<>();
                return temp.shoppingItems;
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
                //TODO: az item-et kell updatelni
                database.shoppingListItemDao().update(listItem);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("ListActivity", "ShoppingItem update was successful");
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onShoppingItemCreated(final ShoppingItem newItem) {
        new AsyncTask<Void, Void, ShoppingItem>() {

            @Override
            protected ShoppingItem doInBackground(Void... voids) {
                int index = database.shoppingListItemDao().getAll().indexOf(item);
                ShoppingListItem temp = database.shoppingListItemDao().get(index);
                temp.shoppingItems.add(newItem);
                database.shoppingListItemDao().update(newItem);
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
                int index = database.shoppingListItemDao().getAll().indexOf(item);
                ShoppingListItem temp = database.shoppingListItemDao().get(index);
                temp.shoppingItems.remove(newItem);
                database.shoppingListItemDao().update(newItem);
                return null;
            }

            @Override
            protected void onPostExecute(ShoppingItem shoppingItem) {
                adapter.deleteItem(shoppingItem);
            }
        }.execute();
    }
}