package com.duan.quanlychamthi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ChamBaiDao {
    Database dtb;
    SQLiteDatabase db;

    public ChamBaiDao(Context context) {
        dtb = new Database(context);
        db = dtb.getWritableDatabase();
    }

    //Lấy toàn bộ list chấm bài
    public ArrayList<ThongTinChamBai> getAll() {
        ArrayList<ThongTinChamBai> list = new ArrayList<>();
        Cursor cs = db.rawQuery("SELECT * FROM thongtinchambai", null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            int soPhieu = cs.getInt(0);
            String hoTenGv = cs.getString(1);
            String sdtGv = cs.getString(2);
            ThongTinChamBai thongTinChamBai = new ThongTinChamBai(soPhieu, hoTenGv, Integer.parseInt(sdtGv));
            list.add(thongTinChamBai);
            cs.moveToNext();
        }
        cs.close();
        return list;
    }

    //Thêm phiếu chấm bài mới
    public boolean them(ThongTinChamBai thongTinChamBai) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("soPhieu", thongTinChamBai.getSoPhieu());
        contentValues.put("maMon", thongTinChamBai.getMaMon());
        contentValues.put("soBai", thongTinChamBai.getSoBai());
        long r = db.insert("thongtinchambai", null, contentValues);
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //Cập nhật tên Lớp mới, không cho sửa mã lớp bởi mã lớp là khóa chính, sửa sẽ lỗi!
    public boolean sua(ThongTinChamBai thongTinChamBai) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("maMon", thongTinChamBai.getMaMon());
        contentValues.put("soBai", thongTinChamBai.getSoBai());
        int r = db.update("thongtinchambai", contentValues, "soPhieu=?", new String[]{String.valueOf(thongTinChamBai.getSoPhieu())});
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //Xóa giáo viên
    public boolean xoa(int soPhieu) {
        int r = db.delete("thongtinchambai", "soPhieu=?", new String[]{String.valueOf(soPhieu)});
        if (r <= 0) {
            return false;
        }
        return true;
    }
}
