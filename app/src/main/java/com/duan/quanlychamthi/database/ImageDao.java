package com.duan.quanlychamthi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.duan.quanlychamthi.model.Image;
import com.duan.quanlychamthi.model.ThongTinChamBai;

import java.util.ArrayList;

public class ImageDao {
    Database dtb;
    SQLiteDatabase db;

    public ImageDao(Context context) {
        dtb = new Database(context);
        db = dtb.getWritableDatabase();
    }

    public ArrayList<Image> getALl() {
        ArrayList<Image> list = new ArrayList<>();
        Cursor cs = db.rawQuery("SELECT * FROM camera", null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            try {
                int id = cs.getInt(0);
                String link = cs.getString(1);
                list.add(new Image(id, link));
                cs.moveToNext();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cs.close();
        return list;
    }

    public boolean updateImage(Image image) {
        ContentValues values = new ContentValues();
        values.put("image", image.getImage());
        int r = db.update("camera", values, "id=?", new String[]{String.valueOf(image.getId())});
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //Xóa giáo viên
    public boolean xoa(int id) {
        int r = db.delete("camera", "id=?", new String[]{String.valueOf(id)});
        if (r <= 0) {
            return false;
        }
        return true;
    }

    //Thêm phiếu chấm bài mới
    public boolean them(Image image) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("image", image.getImage());
        long r = db.insert("camera", null, contentValues);
        if (r <= 0) {
            return false;
        }
        return true;
    }
}
