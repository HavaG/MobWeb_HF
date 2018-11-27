package com.example.havi.shoppinglist.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.havi.shoppinglist.R;
import com.example.havi.shoppinglist.database.ShoppingListItem;

public class NewShoppingListItemDialogFragment extends DialogFragment {

    public static final String TAG = "NewShoppingItemDialogFragment";

    public interface NewShoppingListItemDialogListener {
        void onShoppingListItemCreated(ShoppingListItem newListItem);
    }

    private NewShoppingListItemDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewShoppingListItemDialogListener) {
            listener = (NewShoppingListItemDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewShoppingListItemDialogListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_shopping_list_item)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid()) {
                            listener.onShoppingListItemCreated(getShoppingListItem());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private EditText nameEditText;

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_shopping_list_item, null);
        nameEditText = contentView.findViewById(R.id.ShoppingItemNameEditText);
        return contentView;
    }

    private boolean isValid() {
        return nameEditText.getText().length() > 0;
    }

    private ShoppingListItem getShoppingListItem() {
        ShoppingListItem shoppingListItem = new ShoppingListItem();
        shoppingListItem.name = nameEditText.getText().toString();
        return shoppingListItem;
    }
}