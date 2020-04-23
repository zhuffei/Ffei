package com.example.zhuffei.ffei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.adapter.UserAdapter;
import com.example.zhuffei.ffei.entity.User;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.UrlTool;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class FocusActivity extends AppCompatActivity {

    public static final int FOCUS = 1;

    public static final int FANS = 2;

    private ListView listView;

    private List<User> data;

    private TextView title;

    private ImageView back, empty;

    private String url;

    private int code;

    private ImageView search;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    ToastHelper.showToast("网络异常");
                    break;
                case 0:
                    if (data.isEmpty()) {
                        empty.setVisibility(View.VISIBLE);
                        return;
                    }
                    listView.setAdapter(new UserAdapter(FocusActivity.this, data));
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        listView = findViewById(R.id.listView);
        title = findViewById(R.id.title);
        empty = findViewById(R.id.empty);
        search = findViewById(R.id.searchUser);
        initView();
        initData();
        back = findViewById(R.id.back);
        back.setOnClickListener(v ->
        {
            FocusActivity.this.setResult(1);
            FocusActivity.this.finish();
        });
        search.setOnClickListener(v -> startActivity(new Intent(FocusActivity.this, UserSearchActivity.class)));
    }

    private void initView() {
        code = getIntent().getIntExtra("code", 0);
        url = code == 1 ? UrlTool.LISTFOCUS : UrlTool.LISTFANS;
        title.setText(code == 1 ? "我的关注" : "我的粉丝");
    }

    private void initData() {
        data = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        map.put("uid", FfeiApplication.user.getId());
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                Map<String, Object> map = JSON.parseObject(responseBody);
                List<User> list = JSONObject.parseArray(((JSONArray) map.get("data")).toJSONString(), User.class);
                data = list;
                handler.sendEmptyMessage(0);
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(1);
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
