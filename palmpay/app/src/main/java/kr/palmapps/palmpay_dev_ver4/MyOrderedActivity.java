package kr.palmapps.palmpay_dev_ver4;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kr.palmapps.palmpay_dev_ver4.Adapter.MyOrderedRecyclerViewAdapter;
import kr.palmapps.palmpay_dev_ver4.Item.MyOrderListItem;
import kr.palmapps.palmpay_dev_ver4.Remote.RemoteService;
import kr.palmapps.palmpay_dev_ver4.Remote.ServiceGenerator;
import kr.palmapps.palmpay_dev_ver4.lib.DevLog;
import kr.palmapps.palmpay_dev_ver4.lib.DevToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderedActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    Boolean isNowOrderListClicked = true;

    TextView ordered_now_list;
    TextView ordered_past_list;

    RecyclerView recyclerView_myorder;
    RecyclerView.LayoutManager layoutManager_myorder;

    ArrayList<MyOrderListItem> myOrderNowList = new ArrayList<>();
    ArrayList<MyOrderListItem> myOrderPastList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ordered);

        setViewButtons();
        setMyOrderRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ordered_now_list:
                DevToast.s(getApplicationContext(), "now_orderlist");
                if(!isNowOrderListClicked) {
                    orderListButtonRenderSetter();
                }
                getNowOrderList();
                break;

            case R.id.ordered_past_list:
                DevToast.s(getApplicationContext(), "past_orderlist");
                if(isNowOrderListClicked) {
                    orderListButtonRenderSetter();
                }
                break;
        }
    }

    public void setViewButtons() {
        ordered_now_list = (TextView) findViewById(R.id.ordered_now_list);
        ordered_now_list.setOnClickListener(this);
        ordered_past_list = (TextView) findViewById(R.id.ordered_past_list);
        ordered_past_list.setOnClickListener(this);
    }

    public void orderListButtonRenderSetter() {
        if(isNowOrderListClicked) {
            isNowOrderListClicked = false;
            ordered_now_list.setBackgroundResource(R.drawable.btn_myorder_deactivated);
            ordered_now_list.setTextColor(getResources().getColor(R.color.black));

            ordered_past_list.setBackgroundResource(R.drawable.btn_myorder_activated);
            ordered_past_list.setTextColor(getResources().getColor(R.color.white));
        } else {
            isNowOrderListClicked = true;
            ordered_now_list.setBackgroundResource(R.drawable.btn_myorder_activated);
            ordered_now_list.setTextColor(getResources().getColor(R.color.white));

            ordered_past_list.setBackgroundResource(R.drawable.btn_myorder_deactivated);
            ordered_past_list.setTextColor(getResources().getColor(R.color.black));
        }
    }

    public void setMyOrderRecyclerView() {
        recyclerView_myorder = findViewById(R.id.myOrdered);
        recyclerView_myorder.setHasFixedSize(true);

        layoutManager_myorder = new LinearLayoutManager(this);
        recyclerView_myorder.setLayoutManager(layoutManager_myorder);
    }

    public void orderListRVAdapterSetter(ArrayList<MyOrderListItem> orderList) {
        MyOrderedRecyclerViewAdapter mORV = new MyOrderedRecyclerViewAdapter(orderList);
        recyclerView_myorder.setAdapter(mORV);
    }

    public void getNowOrderList() {
        SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);

        JsonObject emailJsonObject = new JsonObject();
        emailJsonObject.addProperty("email", email);

        DevLog.d(TAG, emailJsonObject.toString());

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<JsonArray> call = remoteService.getNowOrderList(emailJsonObject);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.body() != null) {
                    JsonArray jsonArray = response.body();
                    myOrderNowList = jsonArrayToOrderList(jsonArray);

                    orderListRVAdapterSetter(myOrderNowList);

                } else {
                    Toast.makeText(getApplicationContext(), "현재 주문 내역이 없습니다", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.checknetwork, Toast.LENGTH_LONG).show();
            }
        });
    }

    public ArrayList<MyOrderListItem> jsonArrayToOrderList(JsonArray jsonArray) {
        ArrayList<MyOrderListItem> list = new ArrayList<>();

        MyOrderListItem items;

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject tmpObject = jsonArray.get(i).getAsJsonObject();

            String ordered_store_name = tmpObject.get("ordered_store_name").getAsString();
            String ordered_time       = tmpObject.get("ordered_time").getAsString();
            String ordered_menu_name  = tmpObject.get("ordered_menu_name").getAsString();
            String ordered_count      = tmpObject.get("ordered_count").getAsString();
            String ordered_price      = tmpObject.get("ordered_price").getAsString();

            items = new MyOrderListItem(ordered_store_name, ordered_time, ordered_menu_name, ordered_count, null, ordered_price);

            list.add(items);
        }

        return list;
    }
}
