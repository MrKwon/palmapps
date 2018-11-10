package kr.palmapps.palmpay_dev_ver4.Item;

public class MenuListItem  {

    String menu_name;
    String menu_price;
    String menu_count;

    public MenuListItem(String menu_name, String menu_price) {
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_count = "0";
    }

    public MenuListItem(String menu_name, String menu_price, String menu_count) {
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_count = menu_count;
     }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(String menu_price) {
        this.menu_price = menu_price;
    }

    public String getMenu_count() {
        return menu_count;
    }

    public void setMenu_count(String menu_count) {
        this.menu_count = menu_count;
    }
}
