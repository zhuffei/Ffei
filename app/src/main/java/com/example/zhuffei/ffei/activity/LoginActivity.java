package com.example.zhuffei.ffei.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.entity.User;
import com.example.zhuffei.ffei.tool.DBOpenHelper;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.Tool;
import com.example.zhuffei.ffei.tool.UrlTool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText phoneInput;

    private EditText passwordInput;

    private Button button;

    private ImageView back;

    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneInput = (EditText) findViewById(R.id.phone_input);
        passwordInput = (EditText) findViewById(R.id.password_input);
        button = (Button) findViewById(R.id.btn_login);
        register = (TextView) findViewById(R.id.register);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneInput.getText().toString();
                String password = passwordInput.getText().toString();
                if (phone.isEmpty()) {
                    ToastHelper.showToast(LoginActivity.this, "请输入手机号");
                } else if (password.isEmpty()) {
                    ToastHelper.showToast(LoginActivity.this, "请输入密码");
                } else if (!Tool.isPhoneNumber(phone)) {
                    ToastHelper.showToast(LoginActivity.this, "手机号格式有误");
                } else {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("phone", phone)
                            .add("password", password)
                            .build();
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
                }
            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String text = (String) ((Map) msg.obj).get("msg");
            if (null!=text&&!text.isEmpty()) {
                ToastHelper.showToast(LoginActivity.this, text);
            }
            if ((boolean)((Map) msg.obj).get("state")==true){
                User user = ((JSONObject)((Map) msg.obj).get("data")).toJavaObject(User.class);
//            try{
//            SQLiteDatabase db = new DBOpenHelper(LoginActivity.this).getWritableDatabase();
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("phone",user.getPhone());
//            contentValues.put("pwd",user.getPwd());
//            db.insert("db_user",null,contentValues);}
//            catch (Exception e){
//                ToastHelper.showToast(LoginActivity.this,"保存用户信息失败，下次启动将无法自动登录");
//            }


                SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                sp.edit().putString("name", user.getName())
                        .putString("pwd", user.getPwd())
                        .putString("phone", user.getPhone())
                        .putString("img", user.getImg())
                        .apply();
            }else{
                ToastHelper.showToast(LoginActivity.this,"用户名或密码错误");
            }

        }
    };

}

