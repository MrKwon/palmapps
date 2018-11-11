package kr.palmapps.palmpay_dev_ver4.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.palmapps.palmpay_dev_ver4.Item.MyOrderListItem;
import kr.palmapps.palmpay_dev_ver4.R;

public class MyOrderedRecyclerViewAdapter extends RecyclerView.Adapter<MyOrderedRecyclerViewAdapter.MyOrderedItemviewHolder> {

    ArrayList<MyOrderListItem> items;

    public MyOrderedRecyclerViewAdapter(ArrayList<MyOrderListItem> items) {

        this.items = items;
    }

    @NonNull
    @Override
    public MyOrderedItemviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_myordered, parent, false);
        return new MyOrderedItemviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderedItemviewHolder holder, int position) {
        holder.ordered_store_name   .setText(items.get(position).getOrdered_store_name());
        holder.ordered_time         .setText(items.get(position).getOrdered_time());
        holder.ordered_menu_name    .setText(items.get(position).getOrdered_menu_name());
        holder.ordered_count        .setText(items.get(position).getOrdered_count());
        holder.ordered_price        .setText(items.get(position).getOrdered_price());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyOrderedItemviewHolder extends RecyclerView.ViewHolder {

        private TextView ordered_store_name;
        private TextView ordered_time;
        private TextView ordered_menu_name;
        private TextView ordered_count;
        private ImageView ordered_paytype;
        private TextView ordered_price;

        public MyOrderedItemviewHolder(View itemView) {
            super(itemView);
            this.ordered_store_name = (TextView) itemView.findViewById(R.id.ordered_store_name);
            this.ordered_time       = (TextView) itemView.findViewById(R.id.ordered_time);
            this.ordered_menu_name  = (TextView) itemView.findViewById(R.id.ordered_menu_name);
            this.ordered_count      = (TextView) itemView.findViewById(R.id.ordered_count);
            this.ordered_price      = (TextView) itemView.findViewById(R.id.ordered_price);
        }


    }
}
