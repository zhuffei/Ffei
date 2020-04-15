package com.example.zhuffei.ffei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.service.LoginService;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.Tool;

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
        phoneInput = findViewById(R.id.phone_input);
        passwordInput = findViewById(R.id.password_input);
        button = findViewById(R.id.btn_login);
        register = findViewById(R.id.register);
        back = findViewById(R.id.back);
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
                    ToastHelper.showToast("请输入手机号");
                } else if (password.isEmpty()) {
                    ToastHelper.showToast( "请输入密码");
                } else if (!Tool.isPhoneNumber(phone)) {
                    ToastHelper.showToast( "手机号格式有误");
                } else {
                        LoginService loginService = new LoginService(phone, password, LoginActivity.this);
                        loginService.login();
                }
            }
        });

    }



}

