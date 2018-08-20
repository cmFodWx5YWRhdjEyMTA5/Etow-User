package com.user.etow.models;

import com.google.gson.Gson;

import java.io.Serializable;

public class Driver implements Serializable {

    private int id;
    private String name;
    private String email;
    private String phone;
    private String token;
    private String avatar;
    private String vehicle_number;
    private DriverInfor drivers;
    private int is_free;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public DriverInfor getDrivers() {
        return drivers;
    }

    public void setDrivers(DriverInfor drivers) {
        this.drivers = drivers;
    }

    public int getIs_free() {
        return is_free;
    }

    public void setIs_free(int is_free) {
        this.is_free = is_free;
    }

    public String toJSon(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
