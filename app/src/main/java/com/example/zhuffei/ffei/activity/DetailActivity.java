package com.example.zhuffei.ffei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.tool.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/3/26 13:17
 */
public class DetailActivity extends AppCompatActivity {
    LinearLayout comment ;
    //举报
    LinearLayout report ;
    //收藏
    LinearLayout shoucang;
    ImageView star;
    TextView scText;
    Banner banner;
    Button buy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        List images = new ArrayList();
//        images.add(R.mipmap.m4);
//        images.add(R.mipmap.m9);
//        images.add(R.mipmap.m10);
        banner = (Banner) findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置轮播时间
        banner.setDelayTime(2000);
        //banner设置方法全部调用完毕时最后调用
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.isAutoPlay(false);
        banner.start();
        comment = (LinearLayout) findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               InputTextMsgDialog dialog =  new InputTextMsgDialog(DetailActivity.this,R.style.dialog_center);
                dialog.show();
            }
        });
        //举报
        report = (LinearLayout) findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,ReportActivity.class);
                startActivity(intent);
            }
        });

        //收藏
        shoucang = (LinearLayout) findViewById(R.id.shoucang);
        star = (ImageView) findViewById(R.id.star);
        scText = (TextView) findViewById(R.id.scText);
        shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star.setImageResource(R.mipmap.yishoucang);
                scText.setText("已收藏");
            }
        });
        buy = (Button) findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,BuyActivity.class);
                startActivity(intent);
            }
        });

    }
}