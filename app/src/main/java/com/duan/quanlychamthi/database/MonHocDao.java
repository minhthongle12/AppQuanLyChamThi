package com.duan.quanlychamthi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.duan.quanlychamthi.model.MonHoc;
import com.duan.quanlychamthi.model.PhieuChamBai;

import java.util.ArrayList;

public class MonHocDao {
    Database dtb;
    SQLiteDatabase db;

    public MonHocDao(Context context) {
        dtb = new Database(context);
        db = dtb.getWritableDatabase();
    }

    //Lấy toàn bộ list chấm bài
    public ArrayList<MonHoc> getAll() {
        ArrayList<MonHoc> list = new ArrayList<>();
        Cursor cs = db.rawQuery("SELECT * FROM monhoc", null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            String maMon = cs.getString(0);
            String tenMon = cs.getString(1);
            int chiPhiMon = cs.getInt(2);
            MonHoc monHoc = new MonHoc(maMon, tenMon, chiPhiMon);
            list.add(monHoc);
            cs.moveToNext();
        }
        cs.close();
        return list;
    }

    //Thêm phiếu chấm bài mới
    public boolean them(MonHoc monHoc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("maMon", monHoc.getMaMon());
        contentValues.put("tenMon", monHoc.getTenMon());
        contentValues.put("chiPhiMon", monHoc.getChiPhiMon());
        long r = db.insert("monhoc", null, contentValues);
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //Cập nhật tên Lớp mới, không cho sửa mã lớp bởi mã lớp là khóa chính, sửa sẽ lỗi!
    public boolean sua(MonHoc monHoc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tenMon", monHoc.getTenMon());
        contentValues.put("chiPhiMon", monHoc.getChiPhiMon());
        int r = db.update("monhoc", contentValues, "maMon=?", new String[]{String.valueOf(monHoc.getMaMon())});
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //Xóa giáo viên
    public boolean xoa(String maMon) {
        int r = db.delete("monhoc", "maMon=?", new String[]{maMon});
        //Khi xóa môn, sẽ xóa luôn thông tin chấm chứa môn
        db.delete("thongtinchambai", "maMon=?", new String[]{maMon});
        if (r <= 0) {
            return false;
        }
        return true;
    }
}
