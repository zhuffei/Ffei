package com.example.zhuffei.ffei.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.adapter.ChatAdapter;
import com.netease.nim.uikit.api.NimUIKit;

import java.util.ArrayList;
import java.util.List;


/**
 * 聊天Fragment
 */
public class LTFragment extends BaseFragment {

    private Context context;

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        context = this.getContext();
        View view = inflater.inflate(R.layout.fragment3, container, false);
        listView = view.findViewById(R.id.listView);
        listView.setDivider(null);


        List data= new ArrayList<>();
        data.add(new Object());
        data.add(new Object());


        listView.setAdapter(new ChatAdapter(context,data));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NimUIKit.startP2PSession(context,"zhuffei");
            }
        });
        return view;
    }

    /**
     * 设置对应的内容即可
     * @param tvTitle
     */
    @Override
    protected void setTitle(TextView tvTitle) {
//        tvTitle.setText("聊天界面");
//        tvTitle.setTextSize(30);
//        tvTitle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }
}
