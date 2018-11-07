package kr.palmapps.palmpay_dev_ver4.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.palmapps.palmpay_dev_ver4.Item.MenuListItem;
import kr.palmapps.palmpay_dev_ver4.R;


public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.MenuItemViewHolder> {

    ArrayList<MenuListItem> items;

    public MenuRecyclerViewAdapter(ArrayList<MenuListItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_list, parent, false);

        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        holder.menu_name.setText(items.get(position).getMenu_name());
        holder.menu_price.setText(items.get(position).getMenu_price() + " 원");
        holder.menu_count.setText(items.get(position).getMenu_count());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView menu_image;
        private TextView menu_name;
        private TextView menu_price;
        private TextView menu_count;

        public MenuItemViewHolder(View view) {
            super(view);
            menu_name = view.findViewById(R.id.menu_name);
            menu_price = view.findViewById(R.id.menu_price);
            menu_count = view.findViewById(R.id.menu_price);
        }
    }
}
