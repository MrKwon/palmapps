package kr.palmapps.palmpay_dev_ver4.Adapter;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.palmapps.palmpay_dev_ver4.Item.OrderListItem;
import kr.palmapps.palmpay_dev_ver4.R;
import kr.palmapps.palmpay_dev_ver4.lib.DevLog;
import kr.palmapps.palmpay_dev_ver4.lib.DevToast;

import static kr.palmapps.palmpay_dev_ver4.MainActivity.orderList;

public class OrderlistRecyclerViewAdapter extends RecyclerView.Adapter<OrderlistRecyclerViewAdapter.OrderlistItemViewHolder> {

    ArrayList<OrderListItem> items;

    public OrderlistRecyclerViewAdapter(ArrayList<OrderListItem> items) {

        this.items = items;
    }

    @NonNull
    @Override
    public OrderlistItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_list, parent, false);
        return new OrderlistItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderlistItemViewHolder holder, int position) {
        holder.order_name       .setText(items.get(position).getOrder_name());
        holder.order_each_price .setText(items.get(position).getOrder_each_price());
        holder.order_count      .setText(items.get(position).getOrder_count());

        int eachPrice = Integer.parseInt("1000"/*items.get(position).getOrder_each_price()*/);
        int eachCount = Integer.parseInt(items.get(position).getOrder_count());

        holder.order_total_price.setText(String.valueOf(eachPrice * eachCount));


    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public static class OrderlistItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView order_img;
        private TextView order_name;
        private TextView order_each_price;
        private TextView order_count;
        private TextView order_total_price;

        public String getOrder_name() {
            return order_name.getText().toString();
        }

        public OrderlistItemViewHolder(View itemView){
            super(itemView);
            order_img = (ImageView) itemView.findViewById(R.id.order_img);
            order_name = (TextView) itemView.findViewById(R.id.order_name);
            order_each_price = (TextView) itemView.findViewById(R.id.order_each_price);
            order_count = (TextView) itemView.findViewById(R.id.order_count);
            order_total_price = (TextView) itemView.findViewById(R.id.order_total_price);

        }
    }
}
