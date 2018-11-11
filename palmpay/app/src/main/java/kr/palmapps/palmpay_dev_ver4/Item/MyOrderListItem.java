package kr.palmapps.palmpay_dev_ver4.Item;

import java.util.Date;

public class MyOrderListItem {
    String ordereeStoreName;
    String orderedTime;

    String orderedMenuName;
    String orderedMenuCount;

    String PaymentType;
    String orderedPrice;

    public String getOrdereeStoreName() {
        return ordereeStoreName;
    }

    public void setOrdereeStoreName(String ordereeStoreName) {
        this.ordereeStoreName = ordereeStoreName;
    }

    public String getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(String orderedTime) {
        this.orderedTime = orderedTime;
    }

    public String getOrderedMenuName() {
        return orderedMenuName;
    }

    public void setOrderedMenuName(String orderedMenuName) {
        this.orderedMenuName = orderedMenuName;
    }

    public String getOrderedMenuCount() {
        return orderedMenuCount;
    }

    public void setOrderedMenuCount(String orderedMenuCount) {
        this.orderedMenuCount = orderedMenuCount;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getOrderedPrice() {
        return orderedPrice;
    }

    public void setOrderedPrice(String orderedPrice) {
        this.orderedPrice = orderedPrice;
    }
}
