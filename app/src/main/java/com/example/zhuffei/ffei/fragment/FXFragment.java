package com.example.zhuffei.ffei.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
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

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.activity.SearchActivity;
import com.example.zhuffei.ffei.adapter.GoodsItemAdapter;
import com.example.zhuffei.ffei.entity.GoodsUserVO;
import com.example.zhuffei.ffei.tool.GlideImageLoader;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.Tool;
import com.example.zhuffei.ffei.tool.UrlTool;
import com.example.zhuffei.ffei.view.RefreshRelativeLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 发现Fragment
 */
public class FXFragment extends BaseFragment {

    private List<String> mWarningTextList;

    private RecyclerView recyclerView;

    private List<GoodsUserVO> data;

    private Context mContext;

    private Banner banner;

    private int index = 0;

    private TextView search;

    private TextSwitcher mTextSwitcher;

    private View headerView;

    private RefreshRelativeLayout refresh;

    private int pageNumber = 1;

    private final int pageSize = 10;

    private GoodsItemAdapter adapter;

    private List<Integer> bannerIds = new ArrayList<>();

    private Map<String, Integer> rollTextMap;

    boolean isRefreshing = false;

    private int textIndex = 0;

    Thread thread;

    OkHttpClient client = new OkHttpClient();


    private Handler textHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    if (mWarningTextList.size() == 1) {
                        try {
                            mTextSwitcher.setText(mWarningTextList.get(0));
                        } catch (Exception e) {

                        }
                        index = 0;
                    }
                    if (mWarningTextList.size() > 1) {
//                        handler.postDelayed(
//                        new Runnable() {
//                            @Override
//                            public void run() {
                                try {
                                    mTextSwitcher.setText(mWarningTextList.get(0));
                                } catch (Exception e) {

                                }
                                index = 0;
//                            }};
//                        }, 4000);

                        thread = new Thread() {
                            @Override
                            public void run() {
                                while (textIndex < mWarningTextList.size()) {
                                    synchronized (this) {
                                        SystemClock.sleep(4000);//每隔4秒滚动一次
                                        textHandler.sendEmptyMessage(1);
                                    }
                                }
                            }
                        };
                        thread.start();
                    }
                    break;
                case 1:
                    if (textIndex == mWarningTextList.size()-1) {
                        textIndex = 0;
                    }else{
                        textIndex++;
                    }
                    try {

                        mTextSwitcher.setText(mWarningTextList.get(textIndex));

                    } catch (Exception e) {

                    }

                    break;
                default:
                    break;
            }


        }
    };

    private Handler handler1 = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (null == msg.obj) {
                ToastHelper.showToast("网络异常");
                return;
            }
            List<GoodsUserVO> list = (List<GoodsUserVO>) msg.obj;
            if (list.size() < 10) {
                //没有数据时设置不能上拉加载
                refresh.setPullUpType(RefreshRelativeLayout.PULL_UP_TYPE_NOT_PULL);
            }
            if(list.size()==0) return;
            data.addAll(list);
            pageNumber++;
            if (isRefreshing) return;
            adapter.notifyDataSetChanged();

        }
    };

    private Handler bannerHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            List<GoodsUserVO> list = (List<GoodsUserVO>) ((Map) msg.obj).get("data");
            List<String> images = new ArrayList<>();
            List<String> titles = new ArrayList();
            for (GoodsUserVO g : list) {
                images.add(g.getImg1());
                titles.add(g.getName());
                bannerIds.add(g.getId());
            }
            if ((boolean) ((Map) msg.obj).get("isFirstTime")) {
                //设置banner样式
                banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
                //设置图片加载器
                banner.setImageLoader(new GlideImageLoader());
                //设置图片集合
                banner.setImages(images);
                banner.setBannerTitles(titles);
                //设置轮播时间
                banner.setDelayTime(4000);
                //banner设置方法全部调用完毕时最后调用
                banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
                banner.start();
            } else {
                banner.update(images, titles);
            }
        }
    };

    @Override
    protected void setTitle(TextView tvTitle) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        mContext = this.getActivity();
        findViews(view);
        //设置头部搜索框
        setSearchBar();
        //配置轮播图数据
        initBannerData(true);
        //设置滚动字幕样式
        initTextSwitcher();
        //设置滚动字幕内容
        initTextData();
        initWaterFall();
        //加载推荐商品数据
        initGoodsData();
        //解决滑动冲突
//        recyclerView.setNestedScrollingEnabled(false);
        setListener();
        return view;
    }

    private void initWaterFall() {
        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
        StaggeredGridLayoutManager recyclerViewLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        data = new ArrayList<>();
        //设置adapter
        adapter = new GoodsItemAdapter(data, mContext);
        adapter.setHeaderView(headerView);
        recyclerView.setAdapter(adapter);
    }

    //配置轮播图数据
    private void initBannerData(boolean isFirstTime) {

        Request request = new Request.Builder().url(UrlTool.GETBANNER).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Message msg = new Message();
                Map<String, Object> map = JSON.parseObject(json);
                List<GoodsUserVO> goodsList = JSONObject.parseArray(((JSONArray) map.get("data")).toJSONString(), GoodsUserVO.class);
                map.put("data", goodsList);
                map.put("isFirstTime", isFirstTime);
                msg.obj = map;
                bannerHandler.sendMessage(msg);
            }
        });

    }


    public void findViews(View view) {
        refresh = view.findViewById(R.id.refresh);
        recyclerView = refresh.getRecyclerView();

        headerView = LayoutInflater.from(mContext).inflate(R.layout.fragment1_header, recyclerView, false);

        mTextSwitcher = headerView.findViewById(R.id.text_switcher);
        search = view.findViewById(R.id.search);
        banner = headerView.findViewById(R.id.banner);
//        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }

    public void setListener() {

        mTextSwitcher.setOnClickListener(v -> Tool.toDetail(mContext, rollTextMap.get(mWarningTextList.get(textIndex))));

        banner.setOnBannerListener(position -> {
            Integer gid = bannerIds.get(position);
            if (gid != null) {
                Tool.toDetail(mContext, gid);
            }
        });

        //下拉刷新
        refresh.setOnRefreshListener(() -> {
            isRefreshing = true;
            data .clear();
            pageNumber = 1;
            initBannerData(false);
            initTextData();
            initGoodsData();
            refresh.setRefreshing(false);
            adapter.notifyDataSetChanged();
            refresh.setPullUpType(RefreshRelativeLayout.PULL_UP_TYPE_LOAD_PULL);
            isRefreshing = false;

        });
        //上拉加载
        refresh.setOnLoadListener(() -> {
            initGoodsData();
            refresh.setLoading(false);
        });
    }

    public void setSearchBar() {
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
    }

    //设置滚动字幕格式
    private void initTextSwitcher() {
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

    //设置滚动字幕数据
    private void initTextData() {
        mWarningTextList = new ArrayList<>();
        Request request = new Request.Builder().url(UrlTool.GETROLLTEXT).get().build();
        Call call = client.newCall(request);
        call.enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String json = response.body().string();
                        Message msg = new Message();
                        Map<String, Object> map = JSON.parseObject(json);
                        rollTextMap = (Map<String, Integer>) map.get("data");
                        mWarningTextList = new ArrayList<>(rollTextMap.keySet());
                        textHandler.sendEmptyMessage(0);
                    }
                });
    }

    private void initGoodsData() {
        Map<String, Integer> map = new HashMap<>();
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNumber);
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.LISTRECENTGOODS, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.obj = null;
                handler1.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Message message = new Message();
                String responseBody = response.body().string();
                Map<String, Object> map = JSON.parseObject(responseBody);
                List<GoodsUserVO> goodsList = JSONObject.parseArray(((JSONArray) map.get("data")).toJSONString(), GoodsUserVO.class);
                message.obj = goodsList;
                handler1.sendMessage(message);
            }
        });
    }

}
