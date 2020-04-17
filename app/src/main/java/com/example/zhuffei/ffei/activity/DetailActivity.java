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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.adapter.CommentAdapter;
import com.example.zhuffei.ffei.entity.CommentUserVO;
import com.example.zhuffei.ffei.entity.GoodsUserVO;
import com.example.zhuffei.ffei.tool.AsyncImageLoader;
import com.example.zhuffei.ffei.tool.GlideImageLoader;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.Tool;
import com.example.zhuffei.ffei.tool.UrlTool;
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
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/3/26 13:17
 */
public class DetailActivity extends AppCompatActivity {
    TextView title, describe, browser, userName, createTime, updateTime, location,price;
    LinearLayout comment;
    //举报
    LinearLayout report;
    //收藏
    LinearLayout shoucang;
    ImageView star;
    TextView scText;
    Banner banner;
    Button buy;
    CircleImageView avator;
    ListView listView;
    GoodsUserVO data;
    List<String> images = new ArrayList();
    List<CommentUserVO> comments;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    displayData();
                    initImages();
                    initBanner();
                    break;
                case 1:
                    listView.setAdapter(new CommentAdapter(comments,DetailActivity.this));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Integer gid = intent.getIntExtra("gid", 0);
        getGoodsInfo(gid);
        findViews();
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputTextMsgDialog dialog = new InputTextMsgDialog(DetailActivity.this, R.style.dialog_center);
                dialog.show();
            }
        });
        //举报
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });

        shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star.setImageResource(R.mipmap.yishoucang);
                scText.setText("已收藏");
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, BuyActivity.class);
                startActivity(intent);
            }
        });

    }

    private void findViews() {
        listView = findViewById(R.id.listView);
        price = findViewById(R.id.price);
        title = findViewById(R.id.title);
        describe = findViewById( R.id.describe);
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
        //收藏
        shoucang = findViewById(R.id.shoucang);
        star = findViewById(R.id.star);
        scText = findViewById(R.id.scText);
    }

    private void displayData(){
        price.setText(data.getPrice()+"");
        title.setText(data.getName());
        describe.setText(data.getDes());
        browser.setText("浏览："+data.getBrowse());
        userName.setText(data.getUserName());
        createTime.setText("发布于： "+Tool.parseTime(data.getCreateTime()));
        updateTime.setText("更新于： "+Tool.parseTime(data.getUpdateTime()));
        location.setText(data.getLocation());
        AsyncImageLoader asyncImageLoader = new AsyncImageLoader(this);
        asyncImageLoader.asyncloadImage(avator,UrlTool.AVATOR+data.getAvator());

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

    private void getGoodsInfo(Integer gid) {
        OkHttpClient client = new OkHttpClient();
        Map<String, Integer> map = new HashMap<>();
        map.put("gid", gid);
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.VIEWGOODS, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Map map = JSON.parseObject(json, HashMap.class);
                data = JSON.parseObject(String.valueOf(map.get("data")), GoodsUserVO.class);
                handler.sendEmptyMessage(0);
            }
        });
        HttpUtil.sendHttpRequest(UrlTool.LISTCOMMENTBYGID, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

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
}