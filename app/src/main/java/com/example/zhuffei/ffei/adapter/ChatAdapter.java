package com.example.zhuffei.ffei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.zhuffei.ffei.R;

import java.util.List;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/7 14:51
 */
public class ChatAdapter extends BaseAdapter {
    private Context context;

    List data;
    public ChatAdapter(Context context, List dataList) {
        this.context = context;
        this.data = dataList;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item,null);
        return view;
    }
}
