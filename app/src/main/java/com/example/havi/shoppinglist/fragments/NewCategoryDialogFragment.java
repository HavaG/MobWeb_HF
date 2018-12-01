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
import android.widget.Toast;

import com.example.havi.shoppinglist.Adapter.CategoryAdapter;
import com.example.havi.shoppinglist.R;
import com.example.havi.shoppinglist.database.Category;
import com.example.havi.shoppinglist.database.ShoppingItem;
import com.example.havi.shoppinglist.database.ShoppingListsListDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewCategoryDialogFragment extends DialogFragment {

    public static final String TAG = "NewCategoryDialogFragment";

    private ShoppingListsListDatabase database;
    private List<Category> categories;


    public interface NewCategoryDialogListener {
        void onCategoryCreated(Category newCategory);
    }

    private NewCategoryDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewCategoryDialogListener) {
            listener = (NewCategoryDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewCategoryDialogListener interface!");
        }

        database = ShoppingListsListDatabase.getInstance(getContext());
        loadBackground();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_category)
                .setView(getContentView())
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onCategoryCreated(getCategory());
                        } else {
                            Toast.makeText(getContext(), "Already exisit", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private EditText nameEditText;

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_category, null);
        nameEditText = contentView.findViewById(R.id.CategoryNameEditText);
        return contentView;
    }

    private boolean isValid() {
        boolean exists = false;
        for(int i = 0; i<categories.size(); i++){
            if(categories.get(i).name.equals(nameEditText.getText().toString())){
                exists = true;
            }
        }
        if(nameEditText.getText().length() > 0 && !exists) {
            return true;
        }
        return false;
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


    private Category getCategory() {
        Category category = new Category();
        category.name = nameEditText.getText().toString();
        return category;
    }
}