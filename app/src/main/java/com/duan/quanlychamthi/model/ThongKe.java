package com.duan.quanlychamthi.model;

import java.io.Serializable;

public class ThongKe implements Serializable {
    private String giaoVien, monHoc, soBai, tongTien;

    public ThongKe() {
    }

    public ThongKe(String giaoVien, String monHoc, String soBai, String tongTien) {
        this.giaoVien = giaoVien;
        this.monHoc = monHoc;
        this.soBai = soBai;
        this.tongTien = tongTien;
    }

    public String getGiaoVien() {
        return giaoVien;
    }

    public void setGiaoVien(String giaoVien) {
        this.giaoVien = giaoVien;
    }

    public String getMonHoc() {
        return monHoc;
    }

    public void setMonHoc(String monHoc) {
        this.monHoc = monHoc;
    }

    public String getSoBai() {
        return soBai;
    }

    public void setSoBai(String soBai) {
        this.soBai = soBai;
    }

    public String getTongTien() {
        return tongTien;
    }

    public void setTongTien(String tongTien) {
        this.tongTien = tongTien;
    }

    @Override
    public String toString() {
        return "ThongKe{" +
                "giaoVien='" + giaoVien + '\'' +
                ", monHoc='" + monHoc + '\'' +
                ", soBai='" + soBai + '\'' +
                ", tongTien='" + tongTien + '\'' +
                '}';
    }
}
