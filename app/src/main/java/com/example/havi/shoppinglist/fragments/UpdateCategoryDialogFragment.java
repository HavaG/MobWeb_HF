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
import android.widget.EditText;
import android.widget.Toast;

import com.example.havi.shoppinglist.R;
import com.example.havi.shoppinglist.database.Category;
import com.example.havi.shoppinglist.database.ShoppingListItem;
import com.example.havi.shoppinglist.database.ShoppingListsListDatabase;

import java.util.List;

public class UpdateCategoryDialogFragment extends DialogFragment {

    private Category updateItem;
    public static final String TAG = "UpdateCategoryDialogFragment";

    private ShoppingListsListDatabase database;
    private List<Category> categories;


    public interface UpdateCategoryDialogListener {
        void onItemChanged(Category newCategory);
    }

    private UpdateCategoryDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof UpdateCategoryDialogListener) {
            listener = (UpdateCategoryDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the UpdateCategoryDialogListener interface!");
        }

        database = ShoppingListsListDatabase.getInstance(getContext());
        loadBackground();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            updateItem = (Category) bundle.getSerializable("ITEM");
        }
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
                            updateItem.name = nameEditText.getText().toString();
                            listener.onItemChanged(getCategory());
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
        nameEditText.setText(updateItem.name);
        return contentView;
    }

    private boolean isValid() {
        boolean exists = false;
        for(int i = 0; i<categories.size(); i++){
            if(categories.get(i).name.equals(nameEditText.getText().toString())){
                exists = true;
            }
        }
        return nameEditText.getText().length() > 0 && !exists;
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