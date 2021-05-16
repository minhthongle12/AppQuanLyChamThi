package com.duan.quanlychamthi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.duan.quanlychamthi.model.GiaoVien;

import java.util.ArrayList;

public class GiaoVienDao {
    Database dtb;
    SQLiteDatabase db;

    public GiaoVienDao(Context context) {
        dtb = new Database(context);
        db = dtb.getWritableDatabase();
    }

    //Lấy toàn bộ list giáo viên
    public ArrayList<GiaoVien> getAll() {
        ArrayList<GiaoVien> list = new ArrayList<>();
        Cursor cs = db.rawQuery("SELECT * FROM giaovien", null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            String maGv = cs.getString(0);
            String hoTenGv = cs.getString(1);
            String sdtGv = cs.getString(2);
            String image = cs.getString(3);
            GiaoVien giaoVien = new GiaoVien(maGv, hoTenGv, sdtGv, image);
            list.add(giaoVien);
            cs.moveToNext();
        }
        cs.close();
        return list;
    }

    //Thêm giáo viên mới
    public boolean them(GiaoVien giaoVien) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("maGv", giaoVien.getMaGv());
        contentValues.put("hoTenGv", giaoVien.getHoTenGv());
        contentValues.put("sdtGv", giaoVien.getSdtGv());
        contentValues.put("linkImage", giaoVien.getLinkImage());
        long r = db.insert("giaovien", null, contentValues);
        if (r <= 0) {
            return false;
        }
        return true;
    }



    //Cập nhật tên Lớp mới, không cho sửa mã lớp bởi mã lớp là khóa chính, sửa sẽ lỗi!
    public boolean sua(GiaoVien giaoVien) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("hoTenGv", giaoVien.getHoTenGv());
        contentValues.put("sdtGv", giaoVien.getSdtGv());
        contentValues.put("linkImage", giaoVien.getLinkImage());
        int r = db.update("giaovien", contentValues, "maGv=?", new String[]{giaoVien.getMaGv()});
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //Xóa giáo viên
    public boolean xoa(String maGv) {
        int r = db.delete("giaovien", "maGv=?", new String[]{maGv});
       db.delete("phieuchambai", "maGv=?", new String[]{maGv});

        if (r <= 0) {
            return false;
        }
        return true;
    }
}
