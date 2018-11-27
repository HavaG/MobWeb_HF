package com.example.havi.shoppinglist.listAdapter;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.havi.shoppinglist.R;
import com.example.havi.shoppinglist.database.ShoppingItem;
import com.example.havi.shoppinglist.database.ShoppingListItem;

import java.lang.reflect.Array;
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
    }

    class ShoppingViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        CheckBox isBoughtCheckBox;
        ImageButton removeButton;

        ShoppingListItem listItem;

        ShoppingViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.ShoppingListItemNameTextView);
            isBoughtCheckBox = itemView.findViewById(R.id.ShoppingItemIsBoughtCheckBox);
            removeButton = itemView.findViewById(R.id.ShoppingItemRemoveButton);

            isBoughtCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                    if(listItem != null){
                        listItem.isBought = isChecked;
                        if(isChecked)
                            nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        listener.onItemChanged(listItem);
                    }
                }
            });
        }
    }

    public void addItem(ShoppingListItem item) {
        lists.add(item);
        notifyItemInserted(lists.size() - 1);
    }

    public void update(List<ShoppingListItem> shoppingListItems) {
        lists.clear();
        lists.addAll(shoppingListItems);
        notifyDataSetChanged();
    }
}