package com.example.zhuffei.ffei.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.adapter.GoodsItemAdapter;
import com.example.zhuffei.ffei.entity.Goods;
import com.example.zhuffei.ffei.tool.RecyclerViewClickListener;
import com.example.zhuffei.ffei.tool.Tool;

import java.util.ArrayList;
import java.util.List;


/**
 * 关注Fragment
 */
public class GZFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private List<Goods> data;
    private Context mContext;

    @Override
    protected void setTitle(TextView tvTitle) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        mContext = this.getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
        StaggeredGridLayoutManager recyclerViewLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        initData();

        GoodsItemAdapter adapter = new GoodsItemAdapter(data, mContext);
        //设置adapter
        recyclerView.setAdapter(adapter);
        //解决滑动冲突
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerViewClickListener(mContext, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Tool.toDetail(mContext);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        return view;
    }

    private void initData() {
        data = new ArrayList<>();
        Goods goods;
        goods = new Goods("阿瓦达多无无多", 15.0, R.mipmap.m1);
        data.add(goods);
        goods = new Goods("阿瓦达无多", 125.1, R.mipmap.m2);
        data.add(goods);
        goods = new Goods("阿温大道", 66.66, R.mipmap.m3);
        data.add(goods);
        goods = new Goods("就感飞觉", 489.51, R.mipmap.m4);
        data.add(goods);
        goods = new Goods("反倒是官方", 56.2, R.mipmap.m5);
        data.add(goods);
        goods = new Goods("根深蒂固", 1.00, R.mipmap.m6);
        data.add(goods);
        goods = new Goods("防守打法范玮琪", 6.55, R.mipmap.m7);
        data.add(goods);
        goods = new Goods("富森合同", 98.3, R.mipmap.m8);
        data.add(goods);
        goods = new Goods("看一看UK一天课题与人体", 66.8, R.mipmap.m9);
        data.add(goods);
        goods = new Goods("刘天稳定", 99.9, R.mipmap.m10);
        data.add(goods);

    }
}
