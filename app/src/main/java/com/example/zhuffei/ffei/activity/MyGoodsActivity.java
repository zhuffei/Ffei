package com.example.zhuffei.ffei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.example.zhuffei.ffei.adapter.GoodsAdapter;
import com.example.zhuffei.ffei.entity.Goods;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.Tool;
import com.example.zhuffei.ffei.tool.UrlTool;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyGoodsActivity extends AppCompatActivity {
    private ImageView back, empty;

    private String url;

    List<Goods> data;
    TextView title;
    public static final int RELEASE = 1;
    public static final int COLLECTION = 2;
    public static final int BUY = 3;
    public static final int SELL = 4;
    int code;
    ListView listView;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastHelper.showToast("网络异常");
                    break;
                case 1:
                    if (data.isEmpty()) {
                        empty.setVisibility(View.VISIBLE);
                        return;
                    }
                    GoodsAdapter goodsAdapter = new GoodsAdapter(data, MyGoodsActivity.this,code);
                    goodsAdapter.setOnclickListener(gid -> {
                        Tool.toDetail(MyGoodsActivity.this, gid);
                    });
                    listView.setAdapter(goodsAdapter);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods);
        title = findViewById(R.id.title);
        empty = findViewById(R.id.empty);
        listView = findViewById(R.id.listView);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> MyGoodsActivity.this.finish());
        initView();
//        listView.setAdapter(new GoodsAdapter(goodsList, this));

    }

    void initView() {
        Intent intent = getIntent();
        code = intent.getIntExtra("code", 0);
        switch (code) {
            case RELEASE:
                title.setText("我的发布");
                url = UrlTool.LISTMYGOODS;
                break;
            case COLLECTION:
                title.setText("我的收藏");
                url = UrlTool.LISTCOLLECTGOODS;
                break;
            case BUY:
                title.setText("已购列表");
                url = UrlTool.LISTMYBUY;
                break;
            case SELL:
                url = UrlTool.LISTMYSELL;
                title.setText("已售列表");
                break;
        }
        getData();
    }

    void getData() {
        Map<String, Integer> map = new HashMap<>();
        map.put("uid", FfeiApplication.user.getId());
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                Map<String, Object> map = JSON.parseObject(responseBody);
                data = JSONObject.parseArray(((JSONArray) map.get("data")).toJSONString(), Goods.class);
                handler.sendEmptyMessage(1);
            }
        });
    }

}
