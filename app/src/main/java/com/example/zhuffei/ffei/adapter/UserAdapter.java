package com.example.zhuffei.ffei.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.alibaba.fastjson.JSON;
import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.activity.UserActivity;
import com.example.zhuffei.ffei.entity.User;
import com.example.zhuffei.ffei.tool.AsyncImageLoader;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
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

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/4 11:48
 */
public class UserAdapter extends BaseAdapter {
    Context context;

    List<User> data;


    AsyncImageLoader asyncImageLoader;

    public UserAdapter(Context context, List<User> data) {
        this.context = context;
        this.data = data;
        asyncImageLoader = new AsyncImageLoader(context);
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Map map = null;
            CardView focusCard = null;
            TextView focus = null;
            Integer option = null;
            if (msg.what == 1) {

                map = (Map) msg.obj;
                focusCard = (CardView) map.get("focusCard");
                focus = (TextView) map.get("focus");
                option = (Integer) map.get("option");
            }
            switch (msg.what) {
                case -1:
                    ToastHelper.showToast("网络异常");
                    break;
                case 0:
                    ToastHelper.showToast("操作失败");
                    break;
                case 1:
                    if (option == 0) {
                        ToastHelper.showToast("已取关");
                        focusCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        focus.setTextColor(Color.parseColor("#111111"));
                        focus.setText("关注");
                    } else {
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
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = data.get(position);
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, null);
        CircleImageView avator = view.findViewById(R.id.avator);
        asyncImageLoader.asyncloadImage(avator, UrlTool.AVATOR + user.getAvator());
        TextView userName = view.findViewById(R.id.userName);
        userName.setText(user.getName());
        LinearLayout userlayout = view.findViewById(R.id.user);
        userlayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserActivity.class);
            intent.putExtra("uid", user.getId());
            context.startActivity(intent);
        });

        CardView focusCard = view.findViewById(R.id.focusCard);
        TextView focus = view.findViewById(R.id.focus);
        //检查是否已关注
        if (user.getIsFocused() == 1) {
            focusCard.setCardBackgroundColor(Color.parseColor("#DDDDDD"));
            focus.setTextColor(Color.parseColor("#999999"));
            focus.setText("已关注");
        } else {
            focusCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            focus.setTextColor(Color.parseColor("#111111"));
            focus.setText("关注");
        }


        focusCard.setOnClickListener(v -> {
            String url;
            Integer option;
            if (focus.getText().equals("已关注")) {
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
                    Map map1 = new HashMap();
                    map1.put("focusCard", focusCard);
                    map1.put("focus", focus);
                    map1.put("option", option);
                    Message msg = new Message();
                    msg.obj = map1;
                    msg.what = result;
                    handler.sendMessage(msg);
                }
            });

        });

        return view;
    }
}