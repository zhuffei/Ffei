package com.example.zhuffei.ffei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.entity.Goods;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.UrlTool;
import com.example.zhuffei.ffei.view.MdStyleProgress;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaySuccessActivity extends BaseActivity {

    private int gid;

    private int uid;

    private int code;

    private MdStyleProgress progress;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    ToastHelper.showToast("网络异常");
                case 0:

                    break;
                case 1:
                    if ((Integer) msg.obj == 1) {
                        if (progress.getStatus() != MdStyleProgress.Status.LoadSuccess) {
                            progress.setStatus(MdStyleProgress.Status.LoadSuccess);
                            progress.startAnima();
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    handler.sendEmptyMessage(2);
                                }
                            }.start();

                        }
                    } else {
                        if (progress.getStatus() != MdStyleProgress.Status.LoadFail) {
                            progress.setStatus(MdStyleProgress.Status.LoadFail);
                            ToastHelper.showToast("购买失败");
                            progress.startAnima();
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    handler.sendEmptyMessage(3);
                                }
                            }.start();
                        }
                    }

                    break;
                case 2:
                    if (code == 1) {
                        startActivity(new Intent(PaySuccessActivity.this, MyGoodsActivity.class).putExtra("code", MyGoodsActivity.BUY));
                    } else {
                        startActivity(new Intent(PaySuccessActivity.this, HomeActivity.class));
                    }
                    finish();
                    break;
                case 3:
                    finish();


            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_success_activity);

        progress = findViewById(R.id.progress);

        Intent intent = getIntent();
        gid = intent.getIntExtra("gid", 0);
        uid = intent.getIntExtra("uid", 0);
        code = intent.getIntExtra("code ", 0);

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (code == 1) {
                    buy();
                } else {
                    upWall();
                }
            }

        }.start();

    }

    private void upWall() {
        Map<String, Integer> map = new HashMap<>();
        map.put("gid", gid);
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.UPWALL, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Map map = JSON.parseObject(json, HashMap.class);
                Message msg = new Message();
                msg.obj = (Integer) map.get("data");
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });
    }

    private void buy() {
        Map<String, Integer> map = new HashMap<>();
        map.put("gid", gid);
        map.put("uid", uid);
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.BUY, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Map map = JSON.parseObject(json, HashMap.class);
                Message msg = new Message();
                msg.obj = (Integer) map.get("data");
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });
    }
}