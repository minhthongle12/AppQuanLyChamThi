package com.duan.quanlychamthi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.skyfishjy.library.RippleBackground;
import com.white.progressview.CircleProgressView;
import com.white.progressview.HorizontalProgressView;

import java.util.Timer;
import java.util.TimerTask;


public class Hello extends AppCompatActivity {
    private ImageView logo;
    private TextView text;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        setTitle("QUẢN LÝ CHẤM THI");
        //Tham chiếu id
        logo = findViewById(R.id.logo);
        text = findViewById(R.id.text);

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        rippleBackground.startRippleAnimation();
        CircleProgressView circleProgressView = (CircleProgressView) findViewById(R.id.circle_progress_normal);
        circleProgressView.setReachBarSize(10);
        circleProgressView.setNormalBarSize(20);
        circleProgressView.setOuterSize(20);
        circleProgressView.setTextSize(15);
        circleProgressView.setProgressInTime(0, 3000);
        //Dùng cài đặt sau 2.3 giây màn hình tự chuyển
        Thread bamgio = new Thread() {


            public void run() {
                try {
                    sleep(3000);
                } catch (Exception e) {

                } finally {
                    Intent i = new Intent(Hello.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.bottom, R.anim.nothing);
                }
            }
        };
        bamgio.start();
    }

    //sau khi chuyển sang màn hình chính, kết thúc màn hình chào
    protected void onPause() {
        super.onPause();
    }
}