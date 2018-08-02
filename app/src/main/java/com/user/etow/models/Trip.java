package com.user.etow.models;

import java.io.Serializable;

public class Trip implements Serializable {

    private int id;
    private String pickup_date;
    private String pick_up;
    private String drop_off;
    private String pick_up_latitude;
    private String pick_up_longitude;
    private String drop_off_latitude;
    private String drop_off_longitude;
    private String price;
    private String status;
    private String vehicle_type;
    private String payment_type;
    private User user;
    private String is_schedule ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public void setPickup_date(String pickup_date) {
        this.pickup_date = pickup_date;
    }

    public String getPick_up() {
        return pick_up;
    }

    public void setPick_up(String pick_up) {
        this.pick_up = pick_up;
    }

    public String getDrop_off() {
        return drop_off;
    }

    public void setDrop_off(String drop_off) {
        this.drop_off = drop_off;
    }

    public String getPick_up_latitude() {
        return pick_up_latitude;
    }

    public void setPick_up_latitude(String pick_up_latitude) {
        this.pick_up_latitude = pick_up_latitude;
    }

    public String getPick_up_longitude() {
        return pick_up_longitude;
    }

    public void setPick_up_longitude(String pick_up_longitude) {
        this.pick_up_longitude = pick_up_longitude;
    }

    public String getDrop_off_latitude() {
        return drop_off_latitude;
    }

    public void setDrop_off_latitude(String drop_off_latitude) {
        this.drop_off_latitude = drop_off_latitude;
    }

    public String getDrop_off_longitude() {
        return drop_off_longitude;
    }

    public void setDrop_off_longitude(String drop_off_longitude) {
        this.drop_off_longitude = drop_off_longitude;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIs_schedule() {
        return is_schedule;
    }

    public void setIs_schedule(String is_schedule) {
        this.is_schedule = is_schedule;
    }
}
