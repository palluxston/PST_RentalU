package com.rentalu.pst_rentalu;

import androidx.annotation.NonNull;

public class PropertyAdapter {

    private String list_num;
    private String property_type;
    private String date_time;
    private String bedroom;
    private String furniture;
    private String remark;
    private String reporter_name;

    private byte[] image;
    private Float rent_price;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public PropertyAdapter(String list_num, String property_type, String date_time, String bedroom, String furniture, String remark, String reporter_name, Float rent_price, byte[] image) {
        this.list_num = list_num;
        this.property_type = property_type;
        this.date_time = date_time;
        this.bedroom = bedroom;
        this.furniture = furniture;
        this.remark = remark;
        this.reporter_name = reporter_name;
        this.rent_price = rent_price;
        this.image = image;
    }

    public String getList_num() {
        return list_num;
    }

    public void setList_num(String list_num) {
        this.list_num = list_num;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getBedroom() {
        return bedroom;
    }

    public void setBedroom(String bedroom) {
        this.bedroom = bedroom;
    }

    public String getFurniture() {
        return furniture;
    }

    public void setFurniture(String furniture) {
        this.furniture = furniture;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReporter_name() {
        return reporter_name;
    }

    public void setReporter_name(String reporter_name) {
        this.reporter_name = reporter_name;
    }

    public Float getRent_price() {
        return rent_price;
    }

    public void setRent_price(Float rent_price) {
        this.rent_price = rent_price;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
