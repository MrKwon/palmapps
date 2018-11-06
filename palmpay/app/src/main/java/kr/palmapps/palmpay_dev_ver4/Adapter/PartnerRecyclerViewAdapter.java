package kr.palmapps.palmpay_dev_ver4.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.palmapps.palmpay_dev_ver4.Item.PartnerListItem;
import kr.palmapps.palmpay_dev_ver4.R;

public class PartnerRecyclerViewAdapter extends RecyclerView.Adapter<PartnerRecyclerViewAdapter.PartnerItemViewHolder> {

    ArrayList<PartnerListItem> items;

    public PartnerRecyclerViewAdapter(ArrayList<PartnerListItem> items) {
        this.items = items;
    }

    /**
     * 새로운 뷰 홀더를 생성
     * @param parent RecyclerView 를 포함하고 있는 parent ViewGroup
     * @param viewType viewType
     * @return 새로운 PartnerItemViewHolder를 반환
     */
    @NonNull
    @Override
    public PartnerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_partner_list, parent, false);
        return new PartnerItemViewHolder(view);
    }

    /**
     * View 의 내용을 해당 포지션의 데이터로 바꿈
     * @param holder ViewHolder
     * @param position RecyclerView 내에서의 위치
     */
    @Override
    public void onBindViewHolder(@NonNull PartnerItemViewHolder holder, int position) {
        holder.partner_name.setText(items.get(position).getPartner_name());
        holder.partner_type.setText(items.get(position).getPartner_type());
        holder.partner_desc.setText(items.get(position).getPartner_desc());
    }

    /**
     * items 의 크기 getter
     * @return items의 크기
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class PartnerItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView partner_logo;
        private TextView partner_name;
        private TextView partner_type;
        private TextView partner_desc;

        public PartnerItemViewHolder(View itemView) {
            super(itemView);
            partner_logo = (ImageView) itemView.findViewById(R.id.partner_logo);
            partner_name = (TextView) itemView.findViewById(R.id.partner_name);
            partner_type = (TextView) itemView.findViewById(R.id.partner_type);
            partner_desc = (TextView) itemView.findViewById(R.id.partner_desc);
        }

    }
}

