package com.example.zhuffei.ffei.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.zhuffei.ffei.R;
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

public class RegisterActivity extends AppCompatActivity {

    private Button btn_register;

    private EditText usernameInput;

    private EditText phoneInput;

    private EditText passwordInput;

    private EditText passwordInput2;

    private ImageView back;


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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String phone = phoneInput.getText().toString();
                String password = passwordInput.getText().toString();
                String password2 = passwordInput2.getText().toString();
                if (username.isEmpty() || phone.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                    if (username.isEmpty()) {
                        ToastHelper.showToast( "请输入用户名");
                    } else if (phone.isEmpty()) {
                        ToastHelper.showToast( "请输入手机号");
                    } else if (password.isEmpty()) {
                        ToastHelper.showToast( "请输入密码");
                    } else if (password2.isEmpty()) {
                        ToastHelper.showToast( "请确认密码");
                    }
                } else if (username.length() < 2 || username.length() > 7) {
                    ToastHelper.showToast( "用户名需2-7个字符");
                } else if (!Tool.isPhoneNumber(phone)){
                    ToastHelper.showToast( "手机号格式不正确");
                } else if(!Tool.TestPassword(password)||password.length()<4||password.length()>10){
                    ToastHelper.showToast( "密码长度需要在4-10位之间且只能为数字、字母、符号");
                }else if(!password.equals(password2)){
                    ToastHelper.showToast( "两次输入的密码不一致");
                }else{
                    RequestBody requestBody = new FormBody.Builder()
                            .add("userName", username)
                            .add("password",password)
                            .add("phone",phone)
                            .build();
                    HttpUtil.sendHttpRequest(UrlTool.REGISTER, requestBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message msg = new Message();
                            Map map = new HashMap<>();
                            map.put("msg","连接服务器失败");
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
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String text = (String)((Map)msg.obj).get("msg");
            if(!text.isEmpty()){
                ToastHelper.showToast(text);
            }

        }
    };


}

