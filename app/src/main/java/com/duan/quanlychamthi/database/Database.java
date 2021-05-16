package com.duan.quanlychamthi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    public Database( Context context) {
        super(context, "QUAN_LY_CHAM_THI", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tạo bảng để check tk, mk
        String sql = "CREATE TABLE camera(" +
                "id integer PRIMARY KEY AUTOINCREMENT, " +
                "image text)";
        db.execSQL(sql);

        //Tạo bảng giáo viên
         sql = "CREATE TABLE giaovien(" +
                 "maGv text PRIMARY KEY, " +
                 "hoTenGv text, " +
                 "sdtGv text," +
                 "linkImage text)";
        db.execSQL(sql);

        //Tạo bảng môn học
        sql = "CREATE TABLE monhoc(" +
                "maMon text PRIMARY KEY, " +
                "tenMon text, " +
                "chiPhiMon integer)";
        db.execSQL(sql);

        //Tạo bảng phiếu chấm bài
        sql = "CREATE TABLE phieuchambai(" +
                "soPhieu integer PRIMARY KEY AUTOINCREMENT, " +
                "ngayPhieu text, " +
                "maGv text references giaovien(maGv))";
        db.execSQL(sql);

        //Tạo bảng thông tin chấm bài
        sql = "CREATE TABLE thongtinchambai(" +
                "soPhieu integer PRIMARY KEY references phieuchambai(soPhieu), " +
                "maMon text references monhoc(maMon), " +
                "soBai integer)";
        db.execSQL(sql);

        //Tạo bảng tài khoản
        sql = "CREATE TABLE taiKhoan(" +
                "email text PRIMARY KEY, " +
                "pass text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
