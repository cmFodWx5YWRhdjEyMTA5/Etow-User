package com.user.etow.models;

import java.io.Serializable;

public class Trip implements Serializable {

    private int status;

    public Trip() {
    }

    public Trip(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
