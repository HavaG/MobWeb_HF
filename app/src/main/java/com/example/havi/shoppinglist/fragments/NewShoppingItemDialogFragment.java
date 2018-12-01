package com.example.havi.shoppinglist.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.havi.shoppinglist.R;
import com.example.havi.shoppinglist.database.Category;
import com.example.havi.shoppinglist.database.ShoppingItem;
import com.example.havi.shoppinglist.database.ShoppingListItem;
import com.example.havi.shoppinglist.database.ShoppingListsListDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewShoppingItemDialogFragment extends DialogFragment {

    public static final String TAG = "NewShoppingItemDialogFragment";

    private ShoppingListsListDatabase database;
    private List<Category> categories;
    private NewShoppingItemDialogListener listener;
    ArrayList<String> categoryNames = new ArrayList<>();

    public interface NewShoppingItemDialogListener {
        void onShoppingItemCreated(ShoppingItem newItem);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewShoppingItemDialogListener) {
            listener = (NewShoppingItemDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!");
        }

        database = ShoppingListsListDatabase.getInstance(getContext());
        loadBackground();
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_shopping_item)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onShoppingItemCreated(getShoppingItem());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private EditText nameEditText;
    private CheckBox alreadyPurchasedCheckBox;
    private Spinner categorySpinner;

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_shopping_item, null);
        nameEditText = contentView.findViewById(R.id.ShoppingItemNameEditText);
        alreadyPurchasedCheckBox = contentView.findViewById(R.id.ShoppingItemIsPurchasedCheckBox);

        categorySpinner = contentView.findViewById(R.id.ShoppingItemCategorySpinner);
        for (int i = 0; i < categories.size(); i++){
            categoryNames.add(categories.get(i).name);
        }
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categoryNames));

        return contentView;
    }

    private boolean isValid() {
        if(nameEditText.getText().length() > 0 && categorySpinner.getSelectedItemPosition() != -1)
        {
            return true;
        }
        return false;
    }

    private ShoppingItem getShoppingItem() {
        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.name = nameEditText.getText().toString();
        shoppingItem.isBought = alreadyPurchasedCheckBox.isChecked();
        shoppingItem.category = categories.get(categorySpinner.getSelectedItemPosition());
        return shoppingItem;
    }
}