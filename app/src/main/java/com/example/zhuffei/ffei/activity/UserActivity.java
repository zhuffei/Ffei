package com.example.zhuffei.ffei.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.adapter.GoodsAdapter;
import com.example.zhuffei.ffei.entity.Goods;
import com.example.zhuffei.ffei.entity.User;
import com.example.zhuffei.ffei.tool.AsyncImageLoader;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.Tool;
import com.example.zhuffei.ffei.tool.UrlTool;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserActivity extends AppCompatActivity {
    private ImageView back;
    private ListView listView;
    private List<Goods> data;
    private CircleImageView avator;
    private TextView userName;
    private User user;
    private int uid;
    private CardView focusCard;
    private TextView focus;
    private GoodsAdapter adapter;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    ToastHelper.showToast("网络异常");
                    break;
                case 3:
                    AsyncImageLoader asyncImageLoader = new AsyncImageLoader(UserActivity.this);
                    asyncImageLoader.asyncloadImage(avator, UrlTool.AVATOR + user.getAvator());
                    userName.setText(user.getName());
                    if (user.getIsFocused() == 1) {
                        focusCard.setCardBackgroundColor(Color.parseColor("#DDDDDD"));
                        focus.setTextColor(Color.parseColor("#999999"));
                        focus.setText("已关注");
                    } else {
                        focusCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        focus.setTextColor(Color.parseColor("#111111"));
                        focus.setText("关注");
                    }
                    break;
                case 2:
                    adapter = new GoodsAdapter(data, UserActivity.this, 2);
                    adapter.setOnclickListener(gid -> {
                        Tool.toDetail(UserActivity.this, gid);
                    });
                    listView.setAdapter(adapter);
                    break;
                case 0:
                    ToastHelper.showToast("操作失败");
                    break;
                case 1:
                    int option = (int) msg.obj;
                    if (option == 0) {
                        user.setIsFocused(0);
                        ToastHelper.showToast("已取关");
                        focusCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        focus.setTextColor(Color.parseColor("#111111"));
                        focus.setText("关注");
                    } else {
                        user.setIsFocused(1);
                        ToastHelper.showToast("已关注");
                        focusCard.setCardBackgroundColor(Color.parseColor("#DDDDDD"));
                        focus.setTextColor(Color.parseColor("#999999"));
                        focus.setText("已关注");
                    }

                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        findViews();
        uid = getIntent().getIntExtra("uid", 0);
        back.setOnClickListener(v -> UserActivity.this.finish());
//        listView.setAdapter(new GoodsAdapter(data, UserActivity.this, 1));
        initData();
        setListener();
    }

    void findViews() {
        back = findViewById(R.id.back);
        listView = findViewById(R.id.listView);
        userName = findViewById(R.id.userName);
        avator = findViewById(R.id.avator);
        focus = findViewById(R.id.focus);
        focusCard = findViewById(R.id.focusCard);
    }

    private void initData() {
        Map<String, Integer> map = new HashMap<>();
        map.put("uid", uid);
        map.put("searcher", FfeiApplication.user.getId());
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.GETUSERINFO, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                Map<String, Object> map = JSON.parseObject(responseBody);
                user = JSONObject.parseObject(((JSONObject) map.get("data")).toJSONString(), User.class);
                handler.sendEmptyMessage(3);
            }
        });

        HttpUtil.sendHttpRequest(UrlTool.LISTMYGOODS, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                Map<String, Object> map = JSON.parseObject(responseBody);
                data = JSONObject.parseArray(((JSONArray) map.get("data")).toJSONString(), Goods.class);
                handler.sendEmptyMessage(2);
            }
        });
    }

    void setListener() {
        focusCard.setOnClickListener(v -> {
            String url;
            Integer option;
            if (user.getIsFocused() == 1) {
                url = UrlTool.CANCELFOCUS;
                option = 0;
            } else {
                url = UrlTool.FOCUS;
                option = 1;
            }
            Map<String, Integer> map = new HashMap<>();
            map.put("focuser", FfeiApplication.user.getId());
            map.put("focused", user.getId());
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
                    Integer result = (Integer) map.get("data");
                    Message msg = new Message();
                    msg.obj = option;
                    msg.what = result;
                    handler.sendMessage(msg);
                }
            });
        });
    }
}
