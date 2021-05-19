package com.duan.quanlychamthi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
//    private LinearLayout lnCamera, lnGV, lnPhieuCham, lnThongTin, lnMonHoc, lnThongKe;
    private LinearLayout lnGV;
    private LinearLayout lnMonHoc;
    private Animation blink, l1, l2, l3, l12, l22, l32;
    SharedPreferences pref;
    TextView tvAdmin;
    public static Boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("TRANG CHỦ");
        //Tham chiếu id
        init();
        String admin = getSharedPreferences("account", MODE_PRIVATE).getString("tk", null);
        if (admin != null) {
            //Check admin
            checkAdmin(admin);
            String text = "Xin chào, ";
            if (isAdmin){
            text+="giảng viên "+admin;
            }
            else {
                text+=admin;
            }
            tvAdmin.setText(text);

        }


        if (getIntent().getStringExtra("tk") != null) {
            new ShowDialog(this).show("Đăng nhập thành công!");
        }

        //Sự kiện khi click vào
        //Giáo viên
        lnGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, GiaoVienActivity.class);
                startActivity(i);
                lnGV.startAnimation(blink);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        lnMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MonHocActivity.class);
                startActivity(i);
                lnMonHoc.startAnimation(blink);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }

    private void init() {
        tvAdmin = findViewById(R.id.tvName);
        lnMonHoc = findViewById(R.id.lnMonHoc);
        pref = getSharedPreferences("LOGIN", MODE_PRIVATE);
//        lnCamera = findViewById(R.id.lnCamera);
        lnGV = findViewById(R.id.lnGiaoVien);
//        lnPhieuCham = findViewById(R.id.lnPhieuCham);
//        lnThongTin = findViewById(R.id.lnThongTin);
//        lnMonHoc = findViewById(R.id.lnMonHoc);
//        lnThongKe = findViewById(R.id.lnThongKe);
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.faded);

        l1 = AnimationUtils.loadAnimation(this, R.anim.bot1);
        l2 = AnimationUtils.loadAnimation(this, R.anim.bot2);
        l3 = AnimationUtils.loadAnimation(this, R.anim.bot3);
        l12 = AnimationUtils.loadAnimation(this, R.anim.bot12);
        lnMonHoc.setAnimation(l12);
        l22 = AnimationUtils.loadAnimation(this, R.anim.bot22);
        l32 = AnimationUtils.loadAnimation(this, R.anim.bot32);

        lnGV.setAnimation(l1);
//        lnMonHoc.setAnimation(l12);
//        lnPhieuCham.setAnimation(l2);
//        lnThongTin.setAnimation(l22);
//        lnCamera.setAnimation(l3);
//        lnThongKe.setAnimation(l32);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        menu.findItem(R.id.logout).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkAdmin(String email) {
        if (email != null) {
            try {
                Log.d("MainActivityee", "onStart email: " + email);
                String check = email.split("@")[1];
                if (check.equals("edu.vn") || check.equals("edu.com.vn")) {
                    isAdmin = true;
                } else {
                    isAdmin = false;
                }
                Log.d("MainActivityee", "onStart isAdmin: " + isAdmin);
            } catch (Exception e) {

            }
        }
    }
}