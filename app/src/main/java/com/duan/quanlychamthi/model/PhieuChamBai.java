package com.duan.quanlychamthi.model;

import java.io.Serializable;

public class PhieuChamBai implements Serializable {
    private int soPhieu;
    private String ngayPhieu, maGv;

    public PhieuChamBai() {
    }

    public PhieuChamBai(int soPhieu, String ngayPhieu, String maGv) {
        this.soPhieu = soPhieu;
        this.ngayPhieu = ngayPhieu;
        this.maGv = maGv;
    }

    public int getSoPhieu() {
        return soPhieu;
    }

    public void setSoPhieu(int soPhieu) {
        this.soPhieu = soPhieu;
    }

    public String getNgayPhieu() {
        return ngayPhieu;
    }

    public void setNgayPhieu(String ngayPhieu) {
        this.ngayPhieu = ngayPhieu;
    }

    public String getMaGv() {
        return maGv;
    }

    public void setMaGv(String maGv) {
        this.maGv = maGv;
    }

    @Override
    public String toString() {
        return getSoPhieu()+"";
    }
}
