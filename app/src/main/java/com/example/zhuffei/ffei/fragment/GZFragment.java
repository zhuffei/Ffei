package com.example.zhuffei.ffei.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.activity.LoginActivity;
import com.example.zhuffei.ffei.adapter.GoodsItemAdapter;
import com.example.zhuffei.ffei.entity.GoodsUserVO;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.UrlTool;
import com.example.zhuffei.ffei.view.RefreshRelativeLayout;

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
 * 关注Fragment
 */
public class GZFragment extends BaseFragment {
    private RefreshRelativeLayout refresh;
    private RecyclerView recyclerView;
    private List<GoodsUserVO> data;
    private Context mContext;
    private GoodsItemAdapter adapter;
    private int pageNumber = 1;
    private int pageSize = 10;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    ToastHelper.showToast("网络异常");
                    break;
                case 0:
                    if(((List<GoodsUserVO>)msg.obj).size()<10){
                        refresh.setPullUpType(RefreshRelativeLayout.PULL_UP_TYPE_NOT_PULL);
                    }
                    if(((List<GoodsUserVO>)msg.obj).size()==0) return;
                    adapter.notifyDataSetChanged();
                    pageNumber++;
                    break;
            }
        }
    };

    @Override
    protected void setTitle(TextView tvTitle) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = this.getActivity();
        View view;
        if (!FfeiApplication.isLogin) {
            view = inflater.inflate(R.layout.not_login, container, false);
            Button button = view.findViewById(R.id.button);
            button.setOnClickListener(v -> {
                startActivity(new Intent(mContext, LoginActivity.class));
            });
        } else {
            view = inflater.inflate(R.layout.fragment2, container, false);
            refresh = view.findViewById(R.id.refresh);
            recyclerView = refresh.getRecyclerView();
            data = new ArrayList<>();
            initWaterFall();
            initData();
            setListener();
        }
        return view;
    }

    private void setListener() {
        refresh.setOnLoadListener(() -> {
            initData();
            refresh.setLoading(false);

        });
        refresh.setOnRefreshListener(()->{
            refresh.setPullUpType(RefreshRelativeLayout.PULL_UP_TYPE_LOAD_PULL);
            pageNumber = 1;
            data.clear();
            initData();
            refresh.setRefreshing(false);
        });
    }

    private void initWaterFall() {
        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
        StaggeredGridLayoutManager recyclerViewLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        data = new ArrayList<>();
        //设置adapter
        adapter = new GoodsItemAdapter(data, mContext);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        Map<String, Integer> map = new HashMap<>();
        map.put("uid", FfeiApplication.user.getId());
        map.put("pageNumber",pageNumber);
        map.put("pageSize",pageSize);
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.LISTFOCUSGOODS, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                Map<String, Object> map = JSON.parseObject(responseBody);
                List<GoodsUserVO> goodsList = JSONObject.parseArray(((JSONArray) map.get("data")).toJSONString(), GoodsUserVO.class);
                data.addAll(goodsList);
                Message msg = new Message();
                msg.what = 0;
                msg.obj  = goodsList;
                handler.sendMessage(msg);
            }
        });
    }
}
