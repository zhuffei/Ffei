package com.example.zhuffei.ffei.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.adapter.GoodsItemAdapter;
import com.example.zhuffei.ffei.entity.GoodsUserVO;
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
 * @date 2020/3/26 11:59
 */
public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText param;
    private ImageView search;
    private TextView none;
    private List<GoodsUserVO> data;
    private GoodsItemAdapter adapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    ToastHelper.showToast("网络异常");
                    break;
                case 0:
                    if(data.size()==0){
                        none.setVisibility(View.VISIBLE);
                        break;
                    }
                    none.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findViews();
        initWaterFall();
        setListener();
    }
    private  void findViews(){
        recyclerView = findViewById(R.id.recyclerView);
        param = findViewById(R.id.param);
        search = findViewById(R.id.search);
        none = findViewById(R.id.none);
    }
    private void initWaterFall() {
        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
        StaggeredGridLayoutManager recyclerViewLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        data = new ArrayList<>();
        //设置adapter
        adapter = new GoodsItemAdapter(data, this);
        recyclerView.setAdapter(adapter);
    }

    private void setListener(){
        search.setOnClickListener(v->{
            initData();
        });
    }
    private void initData() {
        if(param.getText().toString().isEmpty()){
            ToastHelper.showToast("请输入搜索内容");
            return;
        }
        Map map = new HashMap<>();
        map.put("param", param.getText().toString());
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.SEARCHGOODS, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                Map<String, Object> map = JSON.parseObject(responseBody);
                List<GoodsUserVO> goodsList = JSONObject.parseArray(((JSONArray) map.get("data")).toJSONString(), GoodsUserVO.class);
                //使用 adapter.notifyDataSetChanged() 时，必须保证传进 Adapter 的数据 List 是同一个 List
                //而不能是其他对象，否则无法更新 listview
                data.clear();
                data.addAll(goodsList);
                handler.sendEmptyMessage(0);

            }
        });
    }
}