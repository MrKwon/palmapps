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
import kr.palmapps.palmpay_dev_ver4.R;
import kr.palmapps.palmpay_dev_ver4.lib.DevLog;

import static kr.palmapps.palmpay_dev_ver4.MainActivity.orderList;


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

        holder.menu_name.setText(items.get(position).getMenu_name());
        holder.menu_price.setText(items.get(position).getMenu_price());
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
            if ( orderList.size() == 0 ) {
                // 단순하게 orderlist 에 삽입
                DevLog.d("ORDER LIST NAME", getMenu_name());
                DevLog.d("ORDER LIST PRICE", getMenu_price());
                DevLog.d("ORDER LIST COUNTS", getMenu_countS());

                OrderListItem tmpOrderItem = new OrderListItem(getMenu_name(), listAdder(), getMenu_price());
                orderList.add(tmpOrderItem);

                DevLog.d("ORDER LIST PROCESSING", tmpOrderItem.toString());

            } else {
                // 주문 내역에 들어있는지 확인해 보아야 함
                int isHere = nameIndexFinder(getMenu_name());
                if ( isHere != -1) {
                    // 해당 아이템을 1 증가시킴
                    int tmpCount = Integer.parseInt(orderList.get(isHere).getOrder_count());
                    orderList.get(isHere).setOrder_count(String.valueOf(tmpCount + 1));
                } else {
                    // 단순하게 orderlist 에 삽입
                    OrderListItem tmpOrderItem = new OrderListItem(getMenu_name(), listAdder(), getMenu_price());
                    orderList.add(tmpOrderItem);
                }


            }
            DevLog.d("ORDER LIST ADD", orderList.toString());
        }

        public void removeGlobalOrderList() {
            int isHere = nameIndexFinder(getMenu_name());

            // 없으면 바로 나감
            if (isHere == -1) {
                return;
            }

            // 있으면 수행
            if (Integer.parseInt(orderList.get(isHere).getOrder_count()) == 1) {
                if(orderList.size() != 1) {
                    orderList.remove(isHere);
                } else {
                    orderList.clear();
                }

            } else if (Integer.parseInt(orderList.get(isHere).getOrder_count()) > 1) {
                int tmpCount = Integer.parseInt(orderList.get(isHere).getOrder_count());
                orderList.get(isHere).setOrder_count(String.valueOf(tmpCount - 1));
            }
        }


        /**
         * string 으로 전달시킨 인자를 전역 orderList 에서 찾고 인덱스를 반환해줌
         * @param string orderList 에서 찾으려는 string
         * @return string 의 index
         *
         * linear seaching algorithm 이긴 하지만 방대한 양을 searching 할 일은 없으니까 추후에 바꾸도록 하지
         */
        public int nameIndexFinder(String string) {
            int i = 0;
            for( ; i < orderList.size(); i++ ) {
                if ( orderList.get(i).getOrder_name() == string ) {
                    return i;
                }
            }
            return -1;
        }

        public String listAdder() {
            return "1";
        }
    }
}
