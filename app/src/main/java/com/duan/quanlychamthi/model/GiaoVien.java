package com.duan.quanlychamthi.model;

import java.io.Serializable;

public class GiaoVien implements Serializable {
    private String maGv, hoTenGv, sdtGv, linkImage;

    public GiaoVien() {
    }

    public GiaoVien(String maGv, String hoTenGv, String sdtGv, String linkImage) {
        this.maGv = maGv;
        this.hoTenGv = hoTenGv;
        this.sdtGv = sdtGv;
        this.linkImage = linkImage;
    }

    public String getMaGv() {
        return maGv;
    }

    public void setMaGv(String maGv) {
        this.maGv = maGv;
    }

    public String getHoTenGv() {
        return hoTenGv;
    }

    public void setHoTenGv(String hoTenGv) {
        this.hoTenGv = hoTenGv;
    }

    public String getSdtGv() {
        return sdtGv;
    }

    public void setSdtGv(String sdtGv) {
        this.sdtGv = sdtGv;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    @Override
    public String toString() {
        return maGv+" | "+hoTenGv;
    }
}
