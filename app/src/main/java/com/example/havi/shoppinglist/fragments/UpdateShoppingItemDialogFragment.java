package com.example.havi.shoppinglist.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.havi.shoppinglist.R;
import com.example.havi.shoppinglist.database.Category;
import com.example.havi.shoppinglist.database.ShoppingItem;
import com.example.havi.shoppinglist.database.ShoppingListsListDatabase;

import java.util.ArrayList;
import java.util.List;

public class UpdateShoppingItemDialogFragment extends DialogFragment {

    private ShoppingItem updateItem;
    public static final String TAG = "UpdateShoppingItemDialogFragment";

    private ShoppingListsListDatabase database;
    private List<Category> categories;
    private UpdateShoppingItemDialogListener listener;
    ArrayList<String> categoryNames = new ArrayList<>();

    public interface UpdateShoppingItemDialogListener {
        void onItemChanged(ShoppingItem newItem);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof UpdateShoppingItemDialogListener) {
            listener = (UpdateShoppingItemDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the UpdateShoppingItemDialogListener interface!");
        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            updateItem = (ShoppingItem) bundle.getSerializable("ITEM");
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
                .setTitle(R.string.update_shopping_item)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            updateItem.name = nameEditText.getText().toString();
                            updateItem.isBought = alreadyPurchasedCheckBox.isChecked();
                            updateItem.category = categories.get(categorySpinner.getSelectedItemPosition());
                            listener.onItemChanged(updateItem);
                        } else {
                            Toast.makeText(getContext(), "Something went wrong. (category? name?)", Toast.LENGTH_SHORT).show();
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
        nameEditText.setText(updateItem.name);
        alreadyPurchasedCheckBox = contentView.findViewById(R.id.ShoppingItemIsPurchasedCheckBox);
        alreadyPurchasedCheckBox.setChecked(updateItem.isBought);

        categorySpinner = contentView.findViewById(R.id.ShoppingItemCategorySpinner);
        for (int i = 0; i < categories.size(); i++){
            categoryNames.add(categories.get(i).name);
        }
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categoryNames));
        int position = categoryNames.indexOf(updateItem.category.name);
        categorySpinner.setSelection(position);

        return contentView;
    }

    private boolean isValid() {
        return nameEditText.getText().length() > 0 && categorySpinner.getSelectedItemPosition() != -1;
    }

    private ShoppingItem getShoppingItem() {
        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.name = nameEditText.getText().toString();
        shoppingItem.isBought = alreadyPurchasedCheckBox.isChecked();
        shoppingItem.category = categories.get(categorySpinner.getSelectedItemPosition());
        return shoppingItem;
    }
}