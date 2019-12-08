package com.example.havi.shoppinglist.Adapter;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.havi.shoppinglist.R;
import com.example.havi.shoppinglist.database.ShoppingListItem;
import com.example.havi.shoppinglist.fragments.UpdateShoppingListItemDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ShoppingAdapter
        extends RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder> {

    private final List<ShoppingListItem> lists;
    private ShoppingListItemClickListener listener;

    public ShoppingAdapter(ShoppingListItemClickListener listener) {
        this.lists = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        ShoppingListItem listItem = lists.get(position);
        holder.nameTextView.setText(listItem.name);
        holder.isBoughtCheckBox.setChecked(listItem.isBought);

        holder.listItem = listItem;
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public interface ShoppingListItemClickListener{
        void onItemChanged(ShoppingListItem list_item);
        void onItemDeleted(ShoppingListItem list_item);
        void onItemClick(ShoppingListItem item);
    }

    class ShoppingViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        CheckBox isBoughtCheckBox;
        ImageButton removeButton;
        ImageButton editButton;
        View nameLayout;

        ShoppingListItem listItem;

        ShoppingViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.ShoppingListItemNameTextView);
            isBoughtCheckBox = itemView.findViewById(R.id.ShoppingItemIsBoughtCheckBox);
            removeButton = itemView.findViewById(R.id.ShoppingItemRemoveButton);
            editButton = itemView.findViewById(R.id.ShoppingItemUpdateButton);
            nameLayout = itemView.findViewById(R.id.nameLayout);

            isBoughtCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                    if(listItem != null){
                        listItem.isBought = isChecked;
                        if(isChecked)
                            nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        else
                            nameTextView.setPaintFlags(nameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                        listener.onItemChanged(listItem);
                    }
                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(listItem);
                    listener.onItemDeleted(listItem);
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    UpdateShoppingListItemDialogFragment fragment = new UpdateShoppingListItemDialogFragment();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ITEM", listItem);
                    fragment.setArguments(bundle);
                    fragment.show(activity.getSupportFragmentManager(), UpdateShoppingListItemDialogFragment.TAG);
                    updateItem(listItem);
                    listener.onItemChanged(listItem);
                }
            });

            nameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(listItem);
                }
            });
        }
    }

    public void addItem(ShoppingListItem item) {
        lists.add(item);
        notifyItemInserted(lists.size() - 1);
    }

    public void deleteItem(ShoppingListItem item) {
        lists.remove(item);
        notifyDataSetChanged();
    }

    public void updateItem(ShoppingListItem item) {
        notifyDataSetChanged();
    }

    public void update(List<ShoppingListItem> shoppingListItems) {
        lists.clear();
        lists.addAll(shoppingListItems);
        notifyDataSetChanged();
    }
}