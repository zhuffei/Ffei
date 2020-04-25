package com.example.zhuffei.ffei.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.UrlTool;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/3 12:49
 */
public class ReportActivity  extends BaseActivity{
    private ImageView back;

    private TextView goodsName ,publisher;

    private EditText reason;

    private Button commit;

    private Integer gid;

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if( msg.what==1){
                ToastHelper.showToast("提交成功");
                ReportActivity.this.finish();
            }else{
                ToastHelper.showToast("提交失败");
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        findViews();
        setListener();
        initData();

    }
    void findViews(){
        back = findViewById(R.id.back);
        goodsName = findViewById(R.id.goodsName);
        publisher = findViewById(R.id.publisher);
        reason = findViewById(R.id.reason);
        commit = findViewById(R.id.commit);
    }
    void setListener(){
        back.setOnClickListener(v -> ReportActivity.this.finish());
        commit.setOnClickListener(v -> {
            if(reason.getText()==null||reason.getText().toString().length()<10){
                ToastHelper.showToast("举报理由不少于10字符！");
            }else{
                Map<String, Object> map = new HashMap<>();
                map.put("gid", gid);
                map.put("uid", FfeiApplication.user.getId());
                map.put("reason",reason.getText().toString());
                String param = JSON.toJSONString(map);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
                HttpUtil.sendHttpRequest(UrlTool.REPORRT, requestBody, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        ToastHelper.showToast("网络异常");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String json = response.body().string();
                        Map map = JSON.parseObject(json, HashMap.class);
                        handler.sendEmptyMessage((Integer)map.get("data"));
                    }
                }) ;
            }
        });
    }
    void initData(){
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        gid = intent.getIntExtra("gid",0);
        String name = intent.getStringExtra("name");
        publisher.setText(userName);
        goodsName.setText(name);
    }
}