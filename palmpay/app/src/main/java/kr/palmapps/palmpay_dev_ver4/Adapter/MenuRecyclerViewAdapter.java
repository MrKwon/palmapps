package kr.palmapps.palmpay_dev_ver4.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import kr.palmapps.palmpay_dev_ver4.Item.MenuListItem;
import kr.palmapps.palmpay_dev_ver4.Item.OrderListItem;
import kr.palmapps.palmpay_dev_ver4.lib.MyApp;
import kr.palmapps.palmpay_dev_ver4.R;
import kr.palmapps.palmpay_dev_ver4.lib.DevLog;

import static kr.palmapps.palmpay_dev_ver4.MainActivity.orderlist;


public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.MenuItemViewHolder> {

    ArrayList<MenuListItem> items;

    public MenuRecyclerViewAdapter(ArrayList<MenuListItem> items)
    {
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
    public void onBindViewHolder(@NonNull final MenuItemViewHolder holder, final int position) {

        final int count = Integer.parseInt(items.get(position).getMenu_count());

        holder.menu_name.setText(items.get(position).getMenu_name());
        holder.menu_price.setText(String.valueOf(count));
        holder.menu_count.setText(items.get(position).getMenu_count());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView menu_image;
        private TextView menu_name;
        private TextView menu_price;

        private ImageView menu_add;
        private ImageView menu_remove;
        private TextView menu_count;

//        HashMap<String, OrderListItem> orderList;

        public MenuItemViewHolder(View view) {
            super(view);
            this.menu_name   = (TextView) view.findViewById(R.id.menu_name);
            this.menu_price  = (TextView) view.findViewById(R.id.menu_price);

            this.menu_add    = (ImageView) view.findViewById(R.id.menu_add);
            this.menu_remove = (ImageView) view.findViewById(R.id.menu_remove);
            this.menu_count  = (TextView) view.findViewById(R.id.menu_count);

            this.menu_add.setOnClickListener(this);
            this.menu_remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.menu_add:
                    addGlobalOrderList();
                    addBtnRoutine(view);
                    break;
                case R.id.menu_remove:
                    removeGlobalOrderList();
                    removeBtnRoutine(view);
                    break;
            }
        }

        public String getMenu_name() {
            return menu_name.getText().toString();
        }

        public String getMenu_price() {
            return menu_price.getText().toString();
        }

        public String getMenu_countS() {
            return menu_count.getText().toString();
        }

        public ImageView getMenu_add() {
            return menu_add;
        }

        public ImageView getMenu_remove() {
            return menu_remove;
        }

        public TextView getMenu_count() {
            return menu_count;
        }

        public int intChanger(TextView textView) {
            int tmp = Integer.parseInt(textView.getText().toString());

            return tmp;
        }

        public void addBtnRoutine(View view) {
            DevLog.d("ORDER LIST BUTTON","menu_add btn Clicked!");
            menu_count.setText(String.valueOf(intChanger(menu_count) + 1));
            String tmp_menu_name = getMenu_name();
            Toast.makeText(view.getContext(), tmp_menu_name, Toast.LENGTH_LONG).show();
        }

        public void removeBtnRoutine(View view) {
            DevLog.d("ORDER LIST BUTTON","menu_remove btn Clicked!");
            if(intChanger(menu_count) != 0){
                menu_count.setText(String.valueOf(intChanger(menu_count) - 1));
            } else {
                Toast.makeText(view.getContext(), "뺄 수량이 없습니다", Toast.LENGTH_LONG).show();
            }
        }

        /**
         * 전역 orderlist에 추가
         */
        public void addGlobalOrderList() {
//            orderList = ((MyApp)getApplication).getOrderList();
            if ( orderlist.size() == 0 ) {
                // 단순하게 orderlist 에 삽입
                MenuListItem tmpMenuItem = new MenuListItem(getMenu_name(), getMenu_price(), getMenu_countS());
                OrderListItem tmpOrderItem = makeMenuToOrderList(tmpMenuItem);
                orderlist.put(getMenu_name(), tmpOrderItem);

            } else {
                // 주문 내역에 들어있는지 확인해 보아야 함
                for(int i = 0; i < orderlist.size(); i++) {
                    if (orderlist.containsKey(getMenu_name())) {
                        // 해당 아이템을 1 증가시킴
                        OrderListItem tmp = orderlist.get(getMenu_name());
                        int tmpCount = Integer.parseInt(tmp.getOrder_count());
                        orderlist.get(getMenu_name()).setOrder_count(String.valueOf(tmpCount + 1));
                    } else {
                        // 단순하게 orderlist 에 삽입
                        MenuListItem tmpMenuItem = new MenuListItem(getMenu_name(), getMenu_price(), getMenu_countS());
                        OrderListItem tmpOrderItem = makeMenuToOrderList(tmpMenuItem);
                        orderlist.put(getMenu_name(), tmpOrderItem);
                    }
                }
            }
            DevLog.d("ORDER LIST ADD", orderlist.get(getMenu_name()).toString());
        }

        public void removeGlobalOrderList() {
            if (orderlist.containsKey(getMenu_name())) {
                if (easyStringToInt(orderlist.get(getMenu_name()).getOrder_count()) == 1) {
                    orderlist.remove(getMenu_name());

                } else if (easyStringToInt(orderlist.get(getMenu_name()).getOrder_count()) > 1) {
                    OrderListItem tmp = orderlist.get(getMenu_name());
                    int tmpCount = Integer.parseInt(tmp.getOrder_count());
                    orderlist.get(getMenu_name()).setOrder_count(String.valueOf(tmpCount - 1));

                }
            }
        }

        /**
         * MenuListItem 을 OrderListItem 으로 변경시켜주는 메서드
         * @param menuListItem
         * @return
         */
        public OrderListItem makeMenuToOrderList(MenuListItem menuListItem) {
            OrderListItem orderListItem = new OrderListItem();

            orderListItem.setOrder_name(menuListItem.getMenu_name());
            orderListItem.setOrder_count(menuListItem.getMenu_count());
            orderListItem.setOrder_each_price(menuListItem.getMenu_count());

            return orderListItem;
        }

        public int easyStringToInt(String string) {

            return Integer.parseInt(string);
        }
    }
}
