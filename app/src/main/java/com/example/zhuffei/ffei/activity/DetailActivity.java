package com.example.zhuffei.ffei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.adapter.CommentAdapter;
import com.example.zhuffei.ffei.entity.CommentUserVO;
import com.example.zhuffei.ffei.entity.GoodsUserVO;
import com.example.zhuffei.ffei.tool.AsyncImageLoader;
import com.example.zhuffei.ffei.tool.GlideImageLoader;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.Tool;
import com.example.zhuffei.ffei.tool.UrlTool;
import com.example.zhuffei.ffei.view.MyListView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/3/26 13:17
 */
public class DetailActivity extends AppCompatActivity {
    TextView title, describe, browser, userName, createTime, updateTime, location, price, none;
    LinearLayout comment;
    //举报
    LinearLayout report;
    //收藏
    LinearLayout shoucang;
    Integer gid;
    ImageView star, back;
    TextView scText;
    Banner banner;
    Button buy, chat;
    CircleImageView avator;
    MyListView listView;
    GoodsUserVO data;
    List<String> images = new ArrayList();
    List<CommentUserVO> comments;
    CommentAdapter adapter;
    ImageView selled;
    private LinearLayout button;
    private LinearLayout buttons;
    private Button chat1;
    boolean isCollected = false;
    public static final int CHECK = 2;
    public static final int ADD = 3;
    public static final int CANCEL = 4;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    ToastHelper.showToast("网络异常");
                    break;
                case 0:
                    displayData();
                    initImages();
                    initBanner();
                    break;
                case 1:
                    if (comments.isEmpty()) {
                        none.setVisibility(View.VISIBLE);
                        break;
                    }
                    adapter = new CommentAdapter(comments, DetailActivity.this);
                    listView.setAdapter(adapter);
                    listView.setAdapter(adapter);
                    adapter.setOnDeletelistener(id -> {
                        Map map = new HashMap();
                        map.put("id", id);
                        String param = JSON.toJSONString(map);
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
                        HttpUtil.sendHttpRequest(UrlTool.DELETECOMMENT, requestBody, new okhttp3.Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                handler.sendEmptyMessage(-1);
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String json = response.body().string();
                                Map map = JSON.parseObject(json, HashMap.class);
                                Integer code = (Integer) map.get("data");
                                Message msg = new Message();
                                msg.obj = code;
                                msg.what = 6;
                                handler.sendMessage(msg);
                            }
                        });
                    });
                    break;
                case CHECK:
                    if ((Integer) msg.obj == 1) {
                        setCollected(true);
                    }
                    break;
                case CANCEL:
                    if ((Integer) msg.obj == 1) {
                        setCollected(false);
                        ToastHelper.showToast("已取消");
                    }
                    break;
                case ADD:
                    if ((Integer) msg.obj == 1) {
                        setCollected(true);
                        ToastHelper.showToast("已收藏");
                    }
                    break;
                case 5:
                    if ((Integer) msg.obj == 1) {
                        ToastHelper.showToast("评论发布成功");
                        refreshComment();
                    } else {
                        ToastHelper.showToast("评论发布失败");
                    }
                    break;
                case 6:
                    if ((Integer) msg.obj == 1) {
                        ToastHelper.showToast("评论已删除");
                        refreshComment();
                    } else {
                        ToastHelper.showToast("评论删除失败");
                    }
                    break;

            }
        }
    };

    private void setCollected(boolean b) {
        if (b) {
            isCollected = true;
            star.setImageResource(R.mipmap.yishoucang);
            scText.setText("已收藏");
        } else {
            isCollected = false;
            star.setImageResource(R.mipmap.shoucang);
            scText.setText("收藏");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        findViews();
        Intent intent = getIntent();
        gid = intent.getIntExtra("gid", 0);
        setListener();
        getGoodsInfo();
        getComments();
        changeCollect(CHECK);
    }

    private void setListener() {

        back.setOnClickListener(v -> DetailActivity.this.finish());
        //评论
        comment.setOnClickListener(v -> {
            InputTextMsgDialog dialog = new InputTextMsgDialog(DetailActivity.this, R.style.dialog_center);
            dialog.setmOnTextSendListener(msg -> {
                if (FfeiApplication.isLogin) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("gid", gid);
                    map.put("uid", FfeiApplication.user.getId());
                    map.put("comment", msg);
                    String param = JSON.toJSONString(map);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
                    HttpUtil.sendHttpRequest(UrlTool.COMMENT, requestBody, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            handler.sendEmptyMessage(-1);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String json = response.body().string();
                            Map map = JSON.parseObject(json, HashMap.class);
                            Integer code = (Integer) map.get("data");
                            Message msg = new Message();
                            msg.obj = code;
                            msg.what = 5;
                            handler.sendMessage(msg);
                        }
                    });
                } else {
                    ToastHelper.showToast("请登录");
                }
            });
            dialog.show();
        });
        //举报
        report.setOnClickListener(v -> {
            if (FfeiApplication.isLogin) {
                Intent intent1 = new Intent(DetailActivity.this, ReportActivity.class);
                intent1.putExtra("gid", gid);
                intent1.putExtra("userName", data.getUserName());
                intent1.putExtra("name", data.getName());
                startActivity(intent1);
            } else {
                ToastHelper.showToast("请登录");
            }
        });

        shoucang.setOnClickListener(v -> changeCollect(isCollected ? CANCEL : ADD));


    }

    private void findViews() {
        back = findViewById(R.id.back);
        none = findViewById(R.id.none);
        listView = findViewById(R.id.listView);
        price = findViewById(R.id.price);
        title = findViewById(R.id.title);
        describe = findViewById(R.id.describe);
        browser = findViewById(R.id.browser);
        userName = findViewById(R.id.userName);
        createTime = findViewById(R.id.createTime);
        updateTime = findViewById(R.id.updateTime);
        location = findViewById(R.id.location);
        report = findViewById(R.id.report);
        comment = findViewById(R.id.comment);
        banner = findViewById(R.id.banner);
        buy = findViewById(R.id.buy);
        avator = findViewById(R.id.avator);
        selled = findViewById(R.id.selled);
        //收藏
        shoucang = findViewById(R.id.shoucang);
        star = findViewById(R.id.star);
        scText = findViewById(R.id.scText);
        listView.setItemsCanFocus(true);
        chat = findViewById(R.id.chat);
        button = findViewById(R.id.button);
        buttons = findViewById(R.id.buttons);
        chat1 = findViewById(R.id.chat1);

    }

    private void displayData() {
        price.setText(String.format("%.2f", data.getPrice()));
        title.setText(data.getName());
        describe.setText(data.getDes());
        browser.setText("浏览：" + data.getBrowse());
        userName.setText(data.getUserName());
        createTime.setText("发布于： " + Tool.parseTime(data.getCreateTime()));
        updateTime.setText("更新于： " + Tool.parseTime(data.getUpdateTime()));
        location.setText(data.getLocation());
        AsyncImageLoader asyncImageLoader = new AsyncImageLoader(this);
        asyncImageLoader.asyncloadImage(avator, UrlTool.AVATOR + data.getAvator());
        boolean isSell = data.getType() == 1;
        boolean isLogin = FfeiApplication.isLogin;
        boolean isSelled = data.getState() > 1;
        if (data.getState() == 0) {
            buttons.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            chat1.setText(R.string.yixiajia);
        } else if (isSelled) {
            buy.setVisibility(View.GONE);
            chat.setVisibility(View.GONE);
            selled.setVisibility(View.VISIBLE);
        } else if (isSell) {
            if (isLogin && isOwner()) {
                chat.setText(R.string.xiajia);
                buy.setText(R.string.shangqiang);
            } else if (isLogin) {
                buy.setOnClickListener(v -> {
                    Intent intent = new Intent(DetailActivity.this, BuyActivity.class);
                    intent.putExtra("gid", data.getId());
                    startActivity(intent);
                    finish();
                });
                chat.setOnClickListener(v -> {
                });
            } else {
                buy.setOnClickListener(v ->
                        ToastHelper.showToast("请登录")
                );
                chat.setOnClickListener(v ->
                        ToastHelper.showToast("请登录")
                );
            }
        } else {
            buttons.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            //求购商品
            if (isLogin && isOwner()) {
                chat1.setText(R.string.xiajia);
                chat1.setOnClickListener(v -> {
                    //下架
                });
            } else if (isLogin) {
                chat1.setOnClickListener(v -> {
                    //聊聊
                });
            }
        }

    }


    private boolean isOwner() {
        return FfeiApplication.user.getId() == data.getuId();
    }

    private void initImages() {
        images.add(data.getImg1());
        images.add(data.getImg2());
        images.add(data.getImg3());
        images.add(data.getImg4());
        images.add(data.getImg5());
        images.add(data.getImg6());
        images.removeIf(Objects::isNull);
    }

    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置轮播时间
        banner.setDelayTime(3000);
        //banner设置方法全部调用完毕时最后调用
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.isAutoPlay(false);
        banner.start();
    }

    private void getGoodsInfo() {
        Map<String, Integer> map = new HashMap<>();
        map.put("gid", gid);
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.VIEWGOODS, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> handler.sendEmptyMessage(-1));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Map map = JSON.parseObject(json, HashMap.class);
                data = JSON.parseObject(String.valueOf(map.get("data")), GoodsUserVO.class);
                handler.sendEmptyMessage(0);
            }
        });

    }

    void getComments() {
        Map<String, Integer> map = new HashMap<>();
        map.put("gid", gid);
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.LISTCOMMENTBYGID, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> handler.sendEmptyMessage(-1));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Map map = JSON.parseObject(json, HashMap.class);
                comments = JSON.parseArray(String.valueOf(map.get("data")), CommentUserVO.class);
                handler.sendEmptyMessage(1);
            }
        });
    }

    void changeCollect(int code) {
        if (FfeiApplication.isLogin) {
            Map<String, Integer> map = new HashMap<>();
            map.put("gid", gid);
            map.put("uid", FfeiApplication.user.getId());
            String param = JSON.toJSONString(map);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
            String url = "";
            switch (code) {
                case CHECK:
                    url = UrlTool.CHECKCOLLECT;
                    break;
                case ADD:
                    url = UrlTool.COLLECT;
                    break;
                case CANCEL:
                    url = UrlTool.CANCELCOLLECT;
                    break;
            }
            HttpUtil.sendHttpRequest(url, requestBody, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    handler.sendEmptyMessage(-1);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String json = response.body().string();
                    Map map = JSON.parseObject(json, HashMap.class);
                    Message msg = new Message();
                    msg.obj = map.get("data");
                    msg.what = code;
                    handler.sendMessage(msg);
                }
            });

        } else {
            if (code != CHECK)
                ToastHelper.showToast("请登录");
        }

    }

    void refreshComment() {
        comments = new ArrayList<>();
        none.setVisibility(View.GONE);
        getComments();
    }
}