package com.example.zhuffei.ffei.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.zhuffei.ffei.R;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;


/**
 * 聊天Fragment
 */
public class LTFragment extends BaseFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


//        View view;
//        if (!FfeiApplication.isLogin) {
//            view = inflater.inflate(R.layout.not_login, container, false);
//            Button button = view.findViewById(R.id.button);
//            button.setOnClickListener(v -> {
//                startActivity(new Intent(context, LoginActivity.class));
//            });
//        } else {
            View view = inflater.inflate(R.layout.fragment3, container, false);
//            listView = view.findViewById(R.id.listView);
//            listView.setDivider(null);
//
//
//            List data = new ArrayList<>();
//            data.add(new Object());
//            data.add(new Object());
//
//
//            listView.setAdapter(new ChatAdapter(context, data));
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    NimUIKit.startP2PSession(context, "zhuffei");
//                }
//            });
//        }
//
        return view;
    }

//    // 将最近联系人列表fragment动态集成进来。
//    private void addRecentContactsFragment() {
//        fragment = new RecentContactsFragment();
//        // 设置要集成联系人列表fragment的布局文件
//        fragment.setContainerId(R.layout.fragment3);
//
//        final UI activity = (UI) getActivity();
//
//        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
//        fragment = (RecentContactsFragment) activity.addFragment(fragment);
//    }
    /**
     * 设置对应的内容即可
     *
     * @param tvTitle
     */
    @Override
    protected void setTitle(TextView tvTitle) {
//        tvTitle.setText("聊天界面");
//        tvTitle.setTextSize(30);
//        tvTitle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }
}
