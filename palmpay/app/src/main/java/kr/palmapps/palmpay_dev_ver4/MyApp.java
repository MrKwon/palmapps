package kr.palmapps.palmpay_dev_ver4;

import android.app.Application;

import kr.palmapps.palmpay_dev_ver4.Item.OrderListItem;

/**
 * 앱 전역에서 사용할 수 있는 클래스
 */
public class MyApp extends Application {

    // 주문 내역은 전역에서 사용해야 버튼으로 수정이 가능함
    private OrderListItem orderListItem;

    public OrderListItem getOrderListItem() {
        return orderListItem;
    }

    public void setOrderListItem(OrderListItem orderListItem) {
        this.orderListItem = orderListItem;
    }
}
