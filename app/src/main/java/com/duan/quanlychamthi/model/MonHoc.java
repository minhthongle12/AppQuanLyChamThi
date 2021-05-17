package com.duan.quanlychamthi.model;

import java.io.Serializable;

public class MonHoc implements Serializable {
    private String maMon, tenMon;
    private int chiPhiMon;

    public MonHoc() {
    }

    public MonHoc(String maMon, String tenMon, int chiPhiMon) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.chiPhiMon = chiPhiMon;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public int getChiPhiMon() {
        return chiPhiMon;
    }

    public void setChiPhiMon(int chiPhiMon) {
        this.chiPhiMon = chiPhiMon;
    }

    @Override
    public String toString() {
        return getMaMon() + " | " + getTenMon();
    }
}
