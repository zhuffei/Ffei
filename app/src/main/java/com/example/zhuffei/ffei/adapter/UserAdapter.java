package com.example.zhuffei.ffei.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.activity.UserActivity;
import com.example.zhuffei.ffei.entity.User;
import com.example.zhuffei.ffei.tool.AsyncImageLoader;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.UrlTool;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/4 11:48
 */
public class UserAdapter extends BaseAdapter {
    Context context;

    List<User> data;

    int code;

    AsyncImageLoader asyncImageLoader;

    public UserAdapter(Context context, List<User> data,int code) {
        this.code = code;
        this.context = context;
        this.data = data;
        asyncImageLoader = new AsyncImageLoader(context);
    }

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
        asyncImageLoader.asyncloadImage(avator, UrlTool.AVATOR+user.getAvator());
        TextView userName = view.findViewById(R.id.userName);
        userName.setText(user.getName());

        LinearLayout userlayout = view.findViewById(R.id.user);
        userlayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserActivity.class);
            intent.putExtra("userId",user.getId());
            context.startActivity(intent);
        });

        final CardView focusCard = view.findViewById(R.id.focusCard);
        final TextView focus = view.findViewById(R.id.focus);
        if(code != 1){
            focusCard.setCardBackgroundColor(Color.parseColor("#DDDDDD"));
            focus.setTextColor(Color.parseColor("#999999"));
            focus.setText("已关注");
        }

        focusCard.setOnClickListener(v -> {
            if (focus.getText().equals("已关注")) {

                ToastHelper.showToast("已取关");
                focusCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                focus.setTextColor(Color.parseColor("#111111"));
                focus.setText("关注");
            } else {
                focusCard.setCardBackgroundColor(Color.parseColor("#DDDDDD"));
                focus.setTextColor(Color.parseColor("#999999"));
                ToastHelper.showToast("已关注");
                focus.setText("已关注");
            }
        });


        return view;
    }
}