package com.duan.quanlychamthi.model;

import java.io.Serializable;

public class ThongTinChamBai implements Serializable {
    private int soPhieu;
    private String maMon;
    private int soBai;

    public ThongTinChamBai() {
    }

    public ThongTinChamBai(int soPhieu, String maMon, int soBai) {
        this.soPhieu = soPhieu;
        this.maMon = maMon;
        this.soBai = soBai;
    }

    public int getSoPhieu() {
        return soPhieu;
    }

    public void setSoPhieu(int soPhieu) {
        this.soPhieu = soPhieu;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public int getSoBai() {
        return soBai;
    }

    public void setSoBai(int soBai) {
        this.soBai = soBai;
    }
}
