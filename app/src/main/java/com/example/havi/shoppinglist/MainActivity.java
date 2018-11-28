package com.example.havi.shoppinglist;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.havi.shoppinglist.database.ShoppingItem;
import com.example.havi.shoppinglist.database.ShoppingListItem;
import com.example.havi.shoppinglist.database.ShoppingListsListDatabase;
import com.example.havi.shoppinglist.fragments.NewShoppingListItemDialogFragment;
import com.example.havi.shoppinglist.listAdapter.ItemAdapter;
import com.example.havi.shoppinglist.listAdapter.ShoppingAdapter;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NewShoppingListItemDialogFragment.NewShoppingListItemDialogListener,
        ShoppingAdapter.ShoppingListItemClickListener{

    private ShoppingListsListDatabase database;
    private RecyclerView listRecyclerView;
    private ShoppingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your Shopping Lists");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewShoppingListItemDialogFragment().show(getSupportFragmentManager(), NewShoppingListItemDialogFragment.TAG);
            }
        });

        database = Room.databaseBuilder(
                getApplicationContext(),
                ShoppingListsListDatabase.class,
                "shopping-lists")
                .fallbackToDestructiveMigration()
                .build();

        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        listRecyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new ShoppingAdapter(this);
        loadItemsInBackground();
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listRecyclerView.setAdapter(adapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<ShoppingListItem>>() {

            @Override
            protected List<ShoppingListItem> doInBackground(Void... voids) {
                return database.shoppingListItemDao().getAll();
            }

            @Override
            protected void onPostExecute(List<ShoppingListItem> shoppingItems) {
                adapter.update(shoppingItems);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onItemChanged(final ShoppingListItem listItem) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.shoppingListItemDao().update(listItem);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("MainActivity", "ShoppingListItem update was successful");
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onShoppingListItemCreated(final ShoppingListItem newItem) {
        new AsyncTask<Void, Void, ShoppingListItem>() {

            @Override
            protected ShoppingListItem doInBackground(Void... voids) {
                newItem.id = database.shoppingListItemDao().insert(newItem);
                return newItem;
            }

            @Override
            protected void onPostExecute(ShoppingListItem shoppingListItem) {
                adapter.addItem(shoppingListItem);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onItemDeleted(final ShoppingListItem newItem) {
        new AsyncTask<Void, Void, ShoppingListItem>() {

            @Override
            protected ShoppingListItem doInBackground(Void... voids) {
                database.shoppingListItemDao().deleteItem(newItem);
                return null;
            }

            @Override
            protected void onPostExecute(ShoppingListItem shoppingListItem) {
                adapter.deleteItem(shoppingListItem);
            }
        }.execute();
    }

    @Override
    public void onItemClick(ShoppingListItem item) {
        //TODO: set listener (open listAcitvity with te given list)
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("item_id", item.id);
        startActivity(intent);
    }
}
