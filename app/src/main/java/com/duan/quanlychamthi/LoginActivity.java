package com.duan.quanlychamthi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.duan.quanlychamthi.database.TaiKhoanDao;
import com.duan.quanlychamthi.model.TaiKhoan;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText user, pass;
    private Button login;
    private TextView register;
    private ShowDialog dialog;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        //Tự truyền tk, mk nếu từ đăng ký quá
        if (getIntent().getStringExtra("tk") != null) {
            dialog.show("Đăng ký thành công!");
            user.setText(getIntent().getStringExtra("tk"));
            pass.setText(getIntent().getStringExtra("mk"));
        }
        else {
            //Tự truyền tk mk nếu đã có
            if (!pref.getString("tk", "").isEmpty() && !pref.getString("mk", "").isEmpty()) {
                user.setText(pref.getString("tk", ""));
                pass.setText(pref.getString("mk", ""));
            }
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tk, mk;
                tk = user.getText().toString();
                mk = pass.getText().toString();
                if (checkValidate(tk, mk)) {
                    TaiKhoanDao taiKhoanDao = new TaiKhoanDao(LoginActivity.this);
                    if (taiKhoanDao.checkLogin(new TaiKhoan(tk, mk))) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("tk", tk);
                        pref.edit().putString("tk", tk).apply();
                        pref.edit().putString("mk", mk).apply();
                        startActivity(intent);
                    } else {
                        dialog.show("Sai tài khoản hoặc mật khẩu");
                    }
                }
                ;
            }
        });
    }


    private void init() {
        user = findViewById(R.id.edtTk);
        pass = findViewById(R.id.edtMk);
        login = findViewById(R.id.btnDangNhap);
        register = findViewById(R.id.tvDangKy);
        dialog = new ShowDialog(this);
        pref = getSharedPreferences("account", MODE_PRIVATE);
    }

    private Boolean checkValidate(String tk, String mk) {
        Boolean check = false;
        if (tk.isEmpty() || mk.isEmpty()) {
            dialog.show("Không được để trống!");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(tk).matches()) {
            dialog.show("Không đúng dạng email!");
        } else {
            check = true;
        }
        return check;
    }
}