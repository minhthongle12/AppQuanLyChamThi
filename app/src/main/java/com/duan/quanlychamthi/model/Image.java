package com.duan.quanlychamthi.model;

import java.io.Serializable;

public class Image implements Serializable {
    private int id;
    private String image;

    public Image() {
    }

    public Image(int id, String image) {
        this.id = id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
