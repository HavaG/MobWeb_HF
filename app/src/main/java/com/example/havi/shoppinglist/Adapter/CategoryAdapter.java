package com.example.havi.shoppinglist.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.havi.shoppinglist.R;
import com.example.havi.shoppinglist.database.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter
        extends RecyclerView.Adapter<CategoryAdapter.ShoppingViewHolder> {

    private final List<Category> categories;

    private CategoryClickListener listener;

    public CategoryAdapter(CategoryClickListener listener) {
        this.categories = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new ShoppingViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        Category categoryItem = categories.get(position);
        holder.nameTextView.setText(categoryItem.name);

        holder.categoryItem = categoryItem;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface CategoryClickListener{
        void onItemDeleted(Category category_item);
    }

    class ShoppingViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageButton removeButton;

        Category categoryItem;

        ShoppingViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.CategoryItemNameTextView);
            removeButton = itemView.findViewById(R.id.CategoryRemoveButton);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(categoryItem);
                    listener.onItemDeleted(categoryItem);
                }
            });
        }
    }

    public void addItem(Category item) {
        categories.add(item);
        notifyItemInserted(categories.size() - 1);
    }

    public void deleteItem(Category item) {
        categories.remove(item);
        notifyDataSetChanged();
    }

    public void update(List<Category> categoryItems) {
        categories.clear();
        categories.addAll(categoryItems);
        notifyDataSetChanged();
    }
}