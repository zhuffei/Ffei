package com.example.zhuffei.ffei.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
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

public class UserSearchActivity extends BaseActivity {
    ImageView back;
    ImageView search;
    EditText param;
    ListView listView;
    List<User> data;
    UserAdapter adapter;
    ImageView empty;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    ToastHelper.showToast("网络异常");
                    break;
                case 0:
                    if(data.isEmpty()){
                        empty.setVisibility(View.VISIBLE);
                        return;
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersearch_activity);
        findViews();
        setListener();
        data = new ArrayList<>();
        adapter = new UserAdapter(this, data);
        listView.setAdapter(adapter);
    }

    void findViews() {
        back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        param = findViewById(R.id.param);
        listView = findViewById(R.id.listView);
        empty = findViewById(R.id.empty);
    }

    void setListener() {
        back.setOnClickListener(v -> this.finish());
        search.setOnClickListener(v -> {
            String parameter = param.getText().toString();
            if (parameter.isEmpty()) {
                ToastHelper.showToast("请输入搜索内容");
                return;
            } else {
                empty.setVisibility(View.GONE);
                data.clear();
                Map<String, Object> map = new HashMap<>();
                map.put("uid", FfeiApplication.user.getId());
                map.put("param", parameter);
                String param = JSON.toJSONString(map);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
                HttpUtil.sendHttpRequest(UrlTool.SEARCHUSER, requestBody, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        handler.sendEmptyMessage(-1);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String responseBody = response.body().string();
                        Map<String, Object> map = JSON.parseObject(responseBody);
                        JSONArray obj = (JSONArray) map.get("data");
                        List<User> list = JSONObject.parseArray(obj.toJSONString(), User.class);
                        Message msg = new Message();
                        data.addAll(list);
                        handler.sendEmptyMessage(0);
                    }
                });
            }
        });
    }
}
