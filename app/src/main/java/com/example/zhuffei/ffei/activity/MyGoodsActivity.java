package com.example.zhuffei.ffei.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.adapter.GoodsAdapter;
import com.example.zhuffei.ffei.entity.Goods;

import java.util.ArrayList;
import java.util.List;

public class MyGoodsActivity extends AppCompatActivity {
    private ImageView back;

    List<Goods> goodsList;
    FrameLayout myFrame;
    int position;
    TextView title;
    public static final int RELEASE = 1;
    public static final int COLLECTION = 2;
    public  static final int BUY = 3;
    public static final int SELL = 4;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods);
        title = (TextView) findViewById(R.id.title);
        position = getIntent().getIntExtra("position", 1);
        listView = (ListView) findViewById(R.id.listView);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyGoodsActivity.this.finish();
            }
        });
        initData(position);
        listView.setAdapter(new GoodsAdapter(goodsList, this));

    }

    void initData(int i) {
        goodsList = new ArrayList<>();
        switch (i) {
            case RELEASE:
                goodsList.add(new Goods("二手笔记本电脑", 65.01, R.mipmap.m1, "已发布"));
                goodsList.add(new Goods("二手笔记本电脑", 65.01, R.mipmap.m2, "待审核"));
                goodsList.add(new Goods("【求购】二手笔记本电脑", 65.01, R.mipmap.qiugou, "已发布"));
                title.setText("我的发布     ");
                break;
            case COLLECTION:
                goodsList.add(new Goods("二手笔记本电脑", 65.01, R.mipmap.m1, "已发布"));
                goodsList.add(new Goods("二手笔记本电脑", 65.01, R.mipmap.m2, "已下架"));
                goodsList.add(new Goods("【求购】二手笔记本电脑", 65.01, R.mipmap.qiugou, "已发布"));
                title.setText("我的收藏     ");
                break;
            case BUY:
                goodsList.add(new Goods("二手笔记本电脑", 65.01, R.mipmap.m1, "订单完成"));
                goodsList.add(new Goods("二手笔记本电脑", 65.01, R.mipmap.m2, "订单完成"));
                goodsList.add(new Goods("【求购】二手笔记本电脑", 65.01, R.mipmap.qiugou, "订单完成"));
                title.setText("已购列表     ");
                break;
            case SELL:
                goodsList.add(new Goods("二手笔记本电脑", 65.01, R.mipmap.m1, "订单完成"));
                goodsList.add(new Goods("二手笔记本电脑", 65.01, R.mipmap.m2, "订单完成"));
                goodsList.add(new Goods("【求购】二手笔记本电脑", 65.01, R.mipmap.qiugou, "订单完成"));
                title.setText("已售列表     ");
                break;
        }
    }
}
