package com.example.havi.shoppinglist;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.example.havi.shoppinglist.database.ShoppingListItem;
import com.example.havi.shoppinglist.database.ShoppingListsListDatabase;
import com.example.havi.shoppinglist.fragments.NewShoppingListItemDialogFragment;
import com.example.havi.shoppinglist.Adapter.ShoppingAdapter;
import com.example.havi.shoppinglist.fragments.UpdateShoppingListItemDialogFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NewShoppingListItemDialogFragment.NewShoppingListItemDialogListener, UpdateShoppingListItemDialogFragment.UpdateShoppingListItemDialogListener,
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

        database = ShoppingListsListDatabase.getInstance(getBaseContext());

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
        if (id == R.id.action_addCategory) {
            Intent intent = new Intent(getBaseContext(), CategoryActivity.class);
            startActivity(intent);
            return true;
        } else if(id == R.id.action_exit) {
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            MainActivity.super.onBackPressed();
                            finishAffinity();
                        }
                    }).create().show();
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
                adapter.updateItem(listItem);
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
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("item_id", item.id);
        startActivity(intent);
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public void onShoppingListItemUpdated(final ShoppingListItem newItem) {
        new AsyncTask<Void, Void, ShoppingListItem>() {

            @Override
            protected ShoppingListItem doInBackground(Void... voids) {
                database.shoppingListItemDao().update(newItem);
                return null;
            }

            @Override
            protected void onPostExecute(ShoppingListItem shoppingListItem) {
                adapter.updateItem(shoppingListItem);
            }
        }.execute();
    }
}
