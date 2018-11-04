package kr.palmapps.palmpay_dev_ver4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyOrderedActivity extends AppCompatActivity {

    private final String TAG = "내 주문내역";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ordered);
    }
}
