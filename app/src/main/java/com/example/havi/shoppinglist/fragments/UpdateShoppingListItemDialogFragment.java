package com.example.havi.shoppinglist.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.havi.shoppinglist.R;
import com.example.havi.shoppinglist.database.ShoppingListItem;

public class UpdateShoppingListItemDialogFragment extends DialogFragment {

    private ShoppingListItem updateItem;
    public static final String TAG = "UpdateShoppingListItemDialogFragment";

    public interface UpdateShoppingListItemDialogListener {
        void onShoppingListItemUpdated(ShoppingListItem newListItem);
    }

    private UpdateShoppingListItemDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof UpdateShoppingListItemDialogListener) {
            listener = (UpdateShoppingListItemDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the UpdateShoppingListItemDialogListener interface!");
        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            updateItem = (ShoppingListItem) bundle.getSerializable("ITEM");
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
                            updateItem.name = nameEditText.getText().toString();
                            listener.onShoppingListItemUpdated(updateItem);
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
        nameEditText.setText(updateItem.name);
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