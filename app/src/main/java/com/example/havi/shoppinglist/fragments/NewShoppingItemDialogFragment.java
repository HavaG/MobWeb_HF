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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.havi.shoppinglist.R;
import com.example.havi.shoppinglist.database.ShoppingItem;
import com.example.havi.shoppinglist.database.ShoppingListItem;

public class NewShoppingItemDialogFragment extends DialogFragment {

    public static final String TAG = "NewShoppingItemDialogFragment";

    public interface NewShoppingItemDialogListener {
        void onShoppingItemCreated(ShoppingItem newItem);
    }

    private NewShoppingItemDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewShoppingItemDialogListener) {
            listener = (NewShoppingItemDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!");
        }
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

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_shopping_item, null);
        nameEditText = contentView.findViewById(R.id.ShoppingItemNameEditText);
        alreadyPurchasedCheckBox = contentView.findViewById(R.id.ShoppingItemIsPurchasedCheckBox);
        return contentView;
    }

    private boolean isValid() {
        return nameEditText.getText().length() > 0;
    }

    private ShoppingItem getShoppingItem() {
        ShoppingItem shoppingItem = new ShoppingItem();
        shoppingItem.name = nameEditText.getText().toString();
        shoppingItem.isBought = alreadyPurchasedCheckBox.isChecked();
        return shoppingItem;
    }
}