package com.example.zhuffei.ffei.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.service.LoginService;
import com.example.zhuffei.ffei.tool.CheckSumBuilder;
import com.example.zhuffei.ffei.tool.Tool;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author zhuffei
 */
public class MainActivity extends AppCompatActivity {

    private Button homePage;

    private Button button2;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setEvent();
        login();
//        new MThread().start();
//        LoadingViewManager.with(this).setHintText("bbbb").setMaxAnimTime(2000).build();
    }

    public void findViews(){
        textView = (TextView) findViewById(R.id.register);
        homePage = (Button) findViewById(R.id.homePage);
        button2 = (Button) findViewById(R.id.login);
    }

    public void login(){
        SharedPreferences sp = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String phone = sp.getString("phone", "");
        String pwd = sp.getString("pwd", "");
        Tool.logout(this);
        if (null!=phone&&!phone.isEmpty()) {
            LoginService loginService = new LoginService(phone,pwd,this);
            loginService.login();
        }
    }
    public void setEvent(){
        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 1);

            }
        });
    }
    public void test() throws Exception {
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

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("accid", "zhuffei1"));
        nvps.add(new BasicNameValuePair("token", "woca.1234"));
        nvps.add(new BasicNameValuePair("name", "朱飞飞"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        // 打印执行结果
        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
    }

    class MThread extends Thread {
        @Override
        public void run() {
            try {
                test();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
