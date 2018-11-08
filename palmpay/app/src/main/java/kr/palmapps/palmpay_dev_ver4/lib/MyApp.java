package kr.palmapps.palmpay_dev_ver4.lib;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

import kr.palmapps.palmpay_dev_ver4.Item.OrderListItem;

/**
 * 앱 전역에서 사용할 수 있는 클래스
 */
public class MyApp extends Application {

    // 주문 내역은 전역에서 사용해야 버튼으로 수정이 가능함
    private static HashMap<String, OrderListItem> orderList;

    public static HashMap<String, OrderListItem> getOrderList() {
        return orderList;
    }

    public void setOrderList(HashMap<String, OrderListItem> orderList) {
        this.orderList = orderList;
    }

    public int size() {
        return orderList.size();
    }

}
