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

import kr.palmapps.palmpay_dev_ver4.Item.MenuListItem;
import kr.palmapps.palmpay_dev_ver4.Item.OrderListItem;
import kr.palmapps.palmpay_dev_ver4.R;
import kr.palmapps.palmpay_dev_ver4.lib.DevLog;


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
        holder.menu_price.setText(String.valueOf(count) + " 원");
        holder.menu_count.setText(items.get(position).getMenu_count());

//        holder.menu_controlLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch(view.getId()){
//                    case R.id.menu_add:
//                        DevLog.d("되고 있는거 맞니?");
//                        holder.menu_count.setText(String.valueOf(count + 1));
//                        break;
//
//                    case R.id. menu_remove:
//                        DevLog.d("되고 있는거 맞니?");
//                        holder.menu_count.setText(String.valueOf(count - 1));
//                        break;
//                }
//            }
//        });
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
                    addBtnRoutine(view);
                    break;
                case R.id.menu_remove:
                    removeBtnRoutine(view);
                    break;
            }
        }

        //        public LinearLayout getMenu_controlLayout() {
//            return menu_controlLayout;
//        }

        public String getMenu_name() {
            return menu_name.getText().toString();
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
            DevLog.d("menu_add btn Clicked!");
            menu_count.setText(String.valueOf(intChanger(menu_count) + 1));
            String tmp_menu_name = getMenu_name();
            Toast.makeText(view.getContext(), tmp_menu_name, Toast.LENGTH_LONG).show();
        }

        public void removeBtnRoutine(View view) {
            DevLog.d("menu_remove btn Clicked!");
            if(intChanger(menu_count) != 0){
                menu_count.setText(String.valueOf(intChanger(menu_count) - 1));
            } else {
                Toast.makeText(view.getContext(), "뺄 수량이 없습니다", Toast.LENGTH_LONG).show();
            }
        }
    }
}
