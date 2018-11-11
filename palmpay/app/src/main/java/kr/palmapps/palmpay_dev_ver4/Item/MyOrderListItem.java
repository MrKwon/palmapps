package kr.palmapps.palmpay_dev_ver4.Item;

public class MyOrderListItem {
    String ordered_store_name;
    String ordered_time;

    String ordered_menu_name;
    String ordered_count;

    String paytype;
    String ordered_price;

    public MyOrderListItem(String s1, String s2, String s3, String s4, String s5, String s6){
        this.ordered_store_name = s1;
        this.ordered_time       = s2;
        this.ordered_menu_name  = s3;
        this.ordered_count      = s4;
        this.paytype            = s5;
        this.ordered_price      = s6;
    }

    public String getOrdered_store_name() {
        return ordered_store_name;
    }

    public void setOrdered_store_name(String ordered_store_name) {
        this.ordered_store_name = ordered_store_name;
    }

    public String getOrdered_time() {
        return ordered_time;
    }

    public void setOrdered_time(String ordered_time) {
        this.ordered_time = ordered_time;
    }

    public String getOrdered_menu_name() {
        return ordered_menu_name;
    }

    public void setOrdered_menu_name(String ordered_menu_name) {
        this.ordered_menu_name = ordered_menu_name;
    }

    public String getOrdered_count() {
        return ordered_count;
    }

    public void setOrdered_count(String ordered_count) {
        this.ordered_count = ordered_count;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getOrdered_price() {
        return ordered_price;
    }

    public void setOrdered_price(String ordered_price) {
        this.ordered_price = ordered_price;
    }
}
