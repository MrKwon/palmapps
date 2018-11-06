package kr.palmapps.palmpay_dev_ver4.Item;

public class PartnerListItem {
    String partner_name;
    String partner_type;
    String partner_desc;

    public PartnerListItem(String name, String type, String desc) {
        this.partner_name = name;
        this.partner_type = type;
        this.partner_desc = desc;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }

    public String getPartner_type() {
        return partner_type;
    }

    public void setPartner_type(String partner_type) {
        this.partner_type = partner_type;
    }

    public String getPartner_desc() {
        return partner_desc;
    }

    public void setPartner_desc(String partner_desc) {
        this.partner_desc = partner_desc;
    }
}
