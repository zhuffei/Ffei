package com.example.zhuffei.ffei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.view.MdStyleProgress;

public class PaySuccessActivity extends AppCompatActivity {

    private MdStyleProgress progress;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (progress.getStatus() != MdStyleProgress.Status.LoadSuccess) {
                        progress.setStatus(MdStyleProgress.Status.LoadSuccess);
                        progress.startAnima();
                    }
                    break;
                case 1:
                    startActivity(new Intent(PaySuccessActivity.this, HomeActivity.class));
                    finish();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_success_activity);

        progress = findViewById(R.id.progress);

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }

        }.start();

//        //btnLoading = (Button) findViewById(R.id.loading);
//        //成功状态
//        btnSuccess.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        //失败状态
//        btnFail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(progress.getStatus() != MdStyleProgress.Status.LoadFail){
//                    progress.setStatus(MdStyleProgress.Status.LoadFail);
//                    progress.failAnima();
//                }
//            }
//        });
    }
}