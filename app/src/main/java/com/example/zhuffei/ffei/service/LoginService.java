package com.example.zhuffei.ffei.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.activity.HomeActivity;
import com.example.zhuffei.ffei.entity.User;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.Tool;
import com.example.zhuffei.ffei.tool.UrlTool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
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
                ToastHelper.showToast(text);
            }
            if(null == ((Map) msg.obj).get("state")){
                ToastHelper.showToast("服务器异常");
                return;
            }
            if (!(boolean) ((Map) msg.obj).get("state")) {
                Tool.logout();
            }else{
                Tool.logout();
                User user = ((JSONObject) ((Map) msg.obj).get("data")).toJavaObject(User.class);
                FfeiApplication.isLogin = true;
                FfeiApplication.user = user;
                //保存登录信息
                SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                sp.edit().putString("name", user.getName())
                        .putString("pwd", user.getPwd())
                        .putString("phone", user.getPhone())
                        .putString("img", user.getImg())
                        .putInt("uId", user.getId())
                        .apply();
                context.startActivity(new Intent(context, HomeActivity.class));
            }



        }
    };


    public LoginService(String phone, String pwd, Context context) {
        this.phone = phone;
        this.pwd = pwd;
        this.context = context;
    }

    public void login() {
        User user = new User();
        user.setPhone(phone);
        user.setPwd(pwd);

        String userJson = JSON.toJSONString(user);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),userJson);


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
            Message msg = new Message();
            Map map = new HashMap<>();
            map.put("msg", "网络异常");
            msg.obj = map;
            handler.sendMessage(msg);
        }
    }
}
