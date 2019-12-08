package com.example.havi.shoppinglist.Adapter;

import android.os.Build;
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
import com.example.havi.shoppinglist.ShoppingItemComparator;
import com.example.havi.shoppinglist.database.ShoppingItem;
import com.example.havi.shoppinglist.database.ShoppingListItem;
import com.example.havi.shoppinglist.fragments.UpdateShoppingItemDialogFragment;
import com.example.havi.shoppinglist.fragments.UpdateShoppingListItemDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter
        extends RecyclerView.Adapter<ItemAdapter.ShoppingViewHolder> {

    private final List<ShoppingItem> lists;

    private ShoppingItemClickListener listener;

    public ItemAdapter(ShoppingItemClickListener listener) {
        this.lists = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.shopping_item, parent, false);
        return new ShoppingViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        ShoppingItem listItem = lists.get(position);
        holder.nameTextView.setText(listItem.name);
        holder.isBoughtCheckBox.setChecked(listItem.isBought);
        holder.categoryTextView.setText(listItem.category.name);

        holder.listItem = listItem;
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public interface ShoppingItemClickListener{
        void onItemChanged(ShoppingItem list_item);
        void onItemDeleted(ShoppingItem list_item);
    }

    class ShoppingViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        CheckBox isBoughtCheckBox;
        ImageButton removeButton;
        ImageButton editButton;
        TextView categoryTextView;

        ShoppingItem listItem;

        ShoppingViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.ShoppingListItemNameTextView);
            categoryTextView = itemView.findViewById(R.id.ShoppingItemCategoryTextView);
            isBoughtCheckBox = itemView.findViewById(R.id.ShoppingItemIsBoughtCheckBox);
            removeButton = itemView.findViewById(R.id.ShoppingItemRemoveButton);
            editButton = itemView.findViewById(R.id.ShoppingItemUpdateButton);

            isBoughtCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                    if(listItem != null){
                        listItem.isBought = isChecked;
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
                    UpdateShoppingItemDialogFragment fragment = new UpdateShoppingItemDialogFragment();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ITEM", listItem);
                    fragment.setArguments(bundle);
                    fragment.show(activity.getSupportFragmentManager(), UpdateShoppingItemDialogFragment.TAG);
                    updateItem(listItem);
                    listener.onItemChanged(listItem);
                }
            });
        }
    }

    public void addItem(ShoppingItem item) {
        lists.add(item);
        notifyItemInserted(lists.size() - 1);
        notifyAdapterDataSetChanged();
    }

    public void deleteItem(ShoppingItem item) {
        lists.remove(item);
        notifyAdapterDataSetChanged();
    }

    public void update(List<ShoppingItem> shoppingItems) {
        lists.clear();
        lists.addAll(shoppingItems);
        notifyAdapterDataSetChanged();
    }

    public void updateItem(ShoppingItem item) {
        notifyAdapterDataSetChanged();
    }

    private void notifyAdapterDataSetChanged() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lists.sort(new ShoppingItemComparator());
        }
        notifyDataSetChanged();
    }
}