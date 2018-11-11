package kr.palmapps.palmpay_dev_ver4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import kr.palmapps.palmpay_dev_ver4.Item.MyOrderListItem;

public class MyOrderedActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    RecyclerView recyclerView_myorder;
    RecyclerView.LayoutManager layoutManager_myorder;

    ArrayList<MyOrderListItem> myOrderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ordered);

        setMyOrderRecyclerView();
    }

    public void setMyOrderRecyclerView() {
        recyclerView_myorder = findViewById(R.id.myOrdered);
        recyclerView_myorder.setHasFixedSize(true);

        layoutManager_myorder = new LinearLayoutManager(this);
        recyclerView_myorder.setLayoutManager(layoutManager_myorder);
    }
}
