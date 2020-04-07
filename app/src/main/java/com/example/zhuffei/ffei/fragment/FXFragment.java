package com.example.zhuffei.ffei.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.activity.SearchActivity;
import com.example.zhuffei.ffei.adapter.GoodsItemAdapter;
import com.example.zhuffei.ffei.entity.Goods;
import com.example.zhuffei.ffei.tool.GlideImageLoader;
import com.example.zhuffei.ffei.tool.RecyclerViewClickListener;
import com.example.zhuffei.ffei.tool.Tool;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * 发现Fragment
 */
public class FXFragment extends BaseFragment {

    private Handler handler = new Handler();
    private boolean isFlipping = false; // 是否启用预警信息轮播
    private List<String> mWarningTextList = new ArrayList<>();

    private RecyclerView recyclerView;

    private List<Goods> data;

    private Context mContext;

    private Banner banner;

    private int index = 0;

    private TextView search;

    private TextSwitcher mTextSwitcher;

    @Override
    protected void setTitle(TextView tvTitle) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        mContext = this.getActivity();
        mTextSwitcher =  view.findViewById(R.id.text_switcher);
        search =  view.findViewById(R.id.search);
        Drawable drawable = getResources().getDrawable(R.mipmap.search);
        //获取屏幕分辨率
        int screenWidth = Tool.getScreenWidth(mContext);
        drawable.setBounds(200 - screenWidth, 0, 250 - screenWidth, 50);
        search.setCompoundDrawables(null, null, drawable, null);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
            }
        });
        List images = new ArrayList<>();
        images.add(R.drawable.s111);
        images.add(R.drawable.s222);
        List titles = new ArrayList();
        titles.add("的网Ada的网");
        titles.add("刚看见没发过");
        banner = (Banner) view.findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        banner.setBannerTitles(titles);
        //设置轮播时间
        banner.setDelayTime(2000);
        //banner设置方法全部调用完毕时最后调用
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        banner.start();
        mWarningTextList.add("求一台二手笔记本能打lol");
        mWarningTextList.add("求一本有笔记的马原书");
        setTextSwitcher();
        setData();

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


    //设置滚动字幕格式
    private void setTextSwitcher() {
        mTextSwitcher.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom));
        mTextSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top));
        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(mContext);
                textView.setSingleLine();
                textView.setTextSize(16);//字号
                textView.setTextColor(Color.parseColor("#ffffff"));
                textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                textView.setSingleLine();
                textView.setGravity(Gravity.CENTER);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                textView.setLayoutParams(params);
                textView.setPadding(25, 0, 25, 0);
                return textView;
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isFlipping) {
                return;
            }
            index++;
            mTextSwitcher.setText(mWarningTextList.get(index % mWarningTextList.size()));
            if (index == mWarningTextList.size()) {
                index = 0;
            }
            startFlipping();
        }
    };

    //开启信息轮播
    public void startFlipping() {
        if (mWarningTextList.size() > 1) {
            handler.removeCallbacks(runnable);
            isFlipping = true;
            handler.postDelayed(runnable, 3000);
        }
    }

    //关闭信息轮播
    public void stopFlipping() {
        if (mWarningTextList.size() > 1) {
            isFlipping = false;
            handler.removeCallbacks(runnable);
        }
    }

    //设置数据
    private void setData() {
        if (mWarningTextList.size() == 1) {
            mTextSwitcher.setText(mWarningTextList.get(0));
            index = 0;
        }
        if (mWarningTextList.size() > 1) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTextSwitcher.setText(mWarningTextList.get(0));
                    index = 0;
                }
            }, 1000);
            mTextSwitcher.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom));
            mTextSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top));
            startFlipping();
        }
    }

    private void initData() {
        data = new ArrayList<>();
        Goods goods;
            goods = new Goods("【求购】阿瓦达多无无多", 15.0, 0);
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


