package com.duan.quanlychamthi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.duan.quanlychamthi.model.PhieuChamBai;
import java.util.ArrayList;

public class PhieuChamDao {
    Database dtb;
    SQLiteDatabase db;

    public PhieuChamDao(Context context) {
        dtb = new Database(context);
        db = dtb.getWritableDatabase();
    }

    //Lấy toàn bộ list chấm bài
    public ArrayList<PhieuChamBai> getAll() {
        ArrayList<PhieuChamBai> list = new ArrayList<>();
        Cursor cs = db.rawQuery("SELECT * FROM phieuchambai", null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            int soPhieu = cs.getInt(0);
            String ngayPhieu = cs.getString(1);
            String maGv = cs.getString(2);
            PhieuChamBai phieuChamBai = new PhieuChamBai(soPhieu, ngayPhieu, maGv);
            list.add(phieuChamBai);
            cs.moveToNext();
        }
        cs.close();
        return list;
    }

    //Thêm phiếu chấm bài mới
    public boolean them(PhieuChamBai phieuChamBai) {
        ContentValues contentValues = new ContentValues();
//        contentValues.put("soPhieu", phieuChamBai.getSoPhieu());
        contentValues.put("ngayPhieu", phieuChamBai.getNgayPhieu());
        contentValues.put("maGv", phieuChamBai.getMaGv());
        long r = db.insert("phieuchambai", null, contentValues);
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //Cập nhật tên Lớp mới, không cho sửa mã lớp bởi mã lớp là khóa chính, sửa sẽ lỗi!
    public boolean sua(PhieuChamBai phieuChamBai) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ngayPhieu", phieuChamBai.getNgayPhieu());
        contentValues.put("maGv", phieuChamBai.getMaGv());
        int r = db.update("phieuchambai", contentValues, "soPhieu=?", new String[]{String.valueOf(phieuChamBai.getSoPhieu())});
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //Xóa giáo viên
    public boolean xoa(int soPhieu) {
        int r = db.delete("phieuchambai", "soPhieu=?", new String[]{String.valueOf(soPhieu)});
        //Khi xóa phiếu chấm, sẽ xóa luôn thông tin chấm thuộc phiếu đó
        db.delete("thongtinchambai", "soPhieu=?", new String[]{String.valueOf(soPhieu)});
        if (r <= 0) {
            return false;
        }
        return true;
    }
}
