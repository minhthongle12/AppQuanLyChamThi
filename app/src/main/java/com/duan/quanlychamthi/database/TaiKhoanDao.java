package com.duan.quanlychamthi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.duan.quanlychamthi.model.TaiKhoan;

import java.util.ArrayList;

public class TaiKhoanDao {
    Database dtb;
    SQLiteDatabase db;

    public TaiKhoanDao(Context context) {
        dtb = new Database(context);
        db = dtb.getWritableDatabase();
    }

    public ArrayList<TaiKhoan> getAll() {
        ArrayList<TaiKhoan> list = new ArrayList<>();
        Cursor cs = db.rawQuery("SELECT * FROM taiKhoan", null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            String email = cs.getString(0);
            String pass = cs.getString(1);
            TaiKhoan taiKhoan = new TaiKhoan(email, pass);
            list.add(taiKhoan);
            cs.moveToNext();
        }
        cs.close();
        return list;
    }


    public boolean them(TaiKhoan taiKhoan) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", taiKhoan.getEmail());
        contentValues.put("pass", taiKhoan.getPass());
        long r = db.insert("taiKhoan", null, contentValues);
        if (r <= 0) {
            return false;
        }
        return true;
    }

    public boolean checkLogin(TaiKhoan taiKhoan) {
        String Query = "Select * from taiKhoan where " + "email" + " = " + "'" + taiKhoan.getEmail() + "'" + " AND pass" + " = " + "'" + taiKhoan.getPass() + "'" ;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();

        return true;
    }


    public boolean sua(TaiKhoan taiKhoan) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("pass", taiKhoan.getPass());
        int r = db.update("taiKhoan", contentValues, "email=?", new String[]{String.valueOf(taiKhoan.getEmail())});
        if (r <= 0) {
            return false;
        }
        return true;
    }

    public boolean xoa(String email) {
        int r = db.delete("taiKhoan", "email=?", new String[]{email});
        if (r <= 0) {
            return false;
        }
        return true;
    }
}
