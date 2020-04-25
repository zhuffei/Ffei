package com.example.zhuffei.ffei.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.entity.User;
import com.example.zhuffei.ffei.service.LoginService;
import com.example.zhuffei.ffei.tool.CheckSumBuilder;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.Tool;
import com.example.zhuffei.ffei.tool.UrlTool;
import com.example.zhuffei.ffei.view.LoadingViewManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {

    private Button btn_register;

    private EditText usernameInput;

    private EditText phoneInput;

    private EditText passwordInput;

    private EditText passwordInput2;

    private ImageView back;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_register = findViewById(R.id.btn_register);
        usernameInput = findViewById(R.id.username_input);
        phoneInput = findViewById(R.id.phone_input);
        passwordInput = findViewById(R.id.password_input);
        passwordInput2 = findViewById(R.id.password_input2);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> RegisterActivity.this.finish());
        btn_register.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String phone = phoneInput.getText().toString();
            String password = passwordInput.getText().toString();
            String password2 = passwordInput2.getText().toString();
            if (username.isEmpty()) {
                ToastHelper.showToast("请输入用户名");
            } else if (username.length() < 2 || username.length() > 7) {
                ToastHelper.showToast("用户名需2-7个字符");
            } else if (phone.isEmpty()) {
                ToastHelper.showToast("请输入手机号");
            } else if (!Tool.isPhoneNumber(phone)) {
                ToastHelper.showToast("手机号格式不正确");
            } else if (password.isEmpty()) {
                ToastHelper.showToast("请输入密码");
            } else if (password2.isEmpty()) {
                ToastHelper.showToast("请确认密码");
            } else if (!Tool.TestPassword(password) || password.length() < 4 || password.length() > 10) {
                ToastHelper.showToast("密码长度需要在4-10位之间且只能为数字、字母、符号");
            } else if (!password.equals(password2)) {
                ToastHelper.showToast("两次输入的密码不一致");
            } else {
                User user = new User();
                user.setName(username);
                user.setPhone(phone);
                user.setPwd(password);
                //开启加载动画
                LoadingViewManager.with(RegisterActivity.this).setHintText("请稍后").setMinAnimTime(1500).build();


                new Thread() {
                    @Override
                    public void run() {
                        try {
                            registerNim(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String text = (String) ((Map) msg.obj).get("msg");
            if (!text.isEmpty()) {
                ToastHelper.showToast(text);
                if (text.equals("注册成功")) {
                    login(user.getPhone(), user.getPwd());
                }
            }

        }
    };

    public void registerNim(User user) throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/user/create.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = "5f87c2f24f10a12a75200cabb84c8f9c";
        String appSecret = "c3d99d7e5dc5";
        String nonce = "12345";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        String accid = UUID.randomUUID().toString();
        accid = accid.replace("-", "");
        // 设置请求的参数
        Log.d("acaaaaaa", accid);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", accid));
        nvps.add(new BasicNameValuePair("token", user.getPwd()));
        nvps.add(new BasicNameValuePair("name", user.getName()));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        // 打印执行结果
        String json = EntityUtils.toString(response.getEntity(), "utf-8");
        Log.d("aaaaaaa", json);
        Map map = JSON.parseObject(json, HashMap.class);
        if ((Integer) map.get("code") == 200) {
            user.setAccid(accid);
            this.user = user;
            register(user);
        } else {
            runOnUiThread(() -> ToastHelper.showToast("注册失败"));
        }
    }

    void register(User user) {
        String param = JSON.toJSONString(user);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.REGISTER, requestBody, new Callback() {
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
    }

    public void login(String phone, String pwd) {
//        SharedPreferences sp = this.getSharedPreferences("user", Context.MODE_PRIVATE);
//        String phone = sp.getString("phone", "");
//        String pwd = sp.getString("pwd", "");
        if (null != phone && !phone.isEmpty()) {
            LoginService loginService = new LoginService(phone, pwd, this);
            loginService.login();
        }
    }

}

