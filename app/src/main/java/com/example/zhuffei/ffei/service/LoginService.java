package com.example.zhuffei.ffei.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.zhuffei.ffei.activity.HomeActivity;
import com.example.zhuffei.ffei.entity.User;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.UrlTool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginService {

    private String phone;

    private String pwd;

    private Context context;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String text = (String) ((Map) msg.obj).get("msg");
            if (null != text && !text.isEmpty()) {
                ToastHelper.showToast(context, text);
            }
            //登录成功
            if (null != ((Map) msg.obj).get("state") &&(boolean) ((Map) msg.obj).get("state") == true) {
                User user = ((JSONObject) ((Map) msg.obj).get("data")).toJavaObject(User.class);

                //保存登录信息
                SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                sp.edit().putString("name", user.getName())
                        .putString("pwd", user.getPwd())
                        .putString("phone", user.getPhone())
                        .putString("img", user.getImg())
                        .apply();
            }else{
                Log.d("aaaaaaa", "aaaaaaaaaa");
                System.out.println(context);
                ToastHelper.showToast(context,"连接服务器失败");
            }

            context.startActivity(new Intent(context, HomeActivity.class));
//            else{
//                ToastHelper.showToast(context,"用户名或密码错误,登陆失败");
//            }

        }
    };

    public LoginService(String phone, String pwd, Context context) {
        this.phone = phone;
        this.pwd = pwd;
        this.context = context;
    }

    public void login() {
        RequestBody requestBody = new FormBody.Builder()
                .add("phone", phone)
                .add("password", pwd)
                .build();
        try {
            HttpUtil.sendHttpRequest(UrlTool.LOGIN, requestBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message msg = new Message();
                    Map map = new HashMap<>();
                    map.put("msg", "连接服务器失败");
                    msg.obj = map;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();
                    Message msg = new Message();
                    Map map = JSON.parseObject(res);
                    msg.obj = map;
                    handler.sendMessage(msg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ToastHelper.showToast(context, "服务器异常");
        }
    }
}
