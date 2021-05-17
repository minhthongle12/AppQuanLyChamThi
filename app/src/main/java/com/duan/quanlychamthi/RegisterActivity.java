package com.duan.quanlychamthi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.duan.quanlychamthi.database.TaiKhoanDao;
import com.duan.quanlychamthi.model.TaiKhoan;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText user, pass, pass2;
    private Button register, close;
    private ShowDialog dialog;
    private String TAG = "RegisterActivity";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        //Khi nhấn nút đóng
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Khi ấn đăng ký
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tk, mk, mk2;
                tk = user.getText().toString();
                mk = pass.getText().toString();
                mk2 = pass2.getText().toString();

                progressDialog.setMessage("Đang đăng ký...");
                progressDialog.show();
                TaiKhoanDao taiKhoanDao = new TaiKhoanDao(RegisterActivity.this);
                if (taiKhoanDao.them(new TaiKhoan(tk, mk))) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("tk", tk);
                    intent.putExtra("mk", mk);
                    progressDialog.dismiss();
                    startActivity(intent);
                }

            }
        });
    }

    private void init() {
        user = findViewById(R.id.edtTkDK);
        pass = findViewById(R.id.edtMkDK);
        pass2 = findViewById(R.id.edtMkDK2);
        register = findViewById(R.id.btnDangKy);
        close = findViewById(R.id.btnNhapLai);
        dialog = new ShowDialog(this);
        progressDialog = new ProgressDialog(this);
    }

   
}