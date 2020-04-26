package com.example.zhuffei.ffei.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.zhuffei.ffei.diymessage.CustomAttachParser;
import com.example.zhuffei.ffei.service.LoginService;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.util.NIMUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/25 21:00
 */
public class WelcomeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(NIMUtil.isMainProcess(this)){
            NimUIKit.init(this);
            NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());
        }else{
            Log.d("aaaaaaaaaa","不是主线程？？？");
        }
        initPermission();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                ToastHelper.showToast("可惜。。。");
                finish();//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
            } else {
                //全部权限通过，可以进行下一步操作。。。
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        SharedPreferences sp = WelcomeActivity.this.getSharedPreferences("user", Context.MODE_PRIVATE);
                        String phone = sp.getString("phone", "");
                        String pwd = sp.getString("pwd", "");
                        if (null != phone && !phone.isEmpty()) {
                            LoginService loginService = new LoginService(phone, pwd, WelcomeActivity.this);
                            loginService.login();
                        } else {
                            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        }
                    }
                }.start();

            }
        }
    }


    //1、首先声明一个数组permissions，将需要的权限都放在里面
    String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    List<String> mPermissionList = new ArrayList<>();

    private final int mRequestCode = 100;//权限请求码

    //权限判断和申请
    private void initPermission() {

        mPermissionList.clear();//清空没有通过的权限

        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }

        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        } else {
            //说明权限都已经通过，可以做你想做的事情去
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences sp = WelcomeActivity.this.getSharedPreferences("user", Context.MODE_PRIVATE);
                    String phone = sp.getString("phone", "");
                    String pwd = sp.getString("pwd", "");
                    if (null != phone && !phone.isEmpty()) {
                        LoginService loginService = new LoginService(phone, pwd, WelcomeActivity.this);
                        loginService.login();
                    } else {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    }
                }
            }.start();
        }
    }


}
