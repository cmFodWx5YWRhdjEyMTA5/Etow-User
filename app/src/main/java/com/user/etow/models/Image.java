package com.user.etow.models;

import android.graphics.Bitmap;

public class Image {

    private String path;
    private Bitmap bitmap;

    public Image() {
    }

    public Image(String path, Bitmap bitmap) {
        this.path = path;
        this.bitmap = bitmap;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
