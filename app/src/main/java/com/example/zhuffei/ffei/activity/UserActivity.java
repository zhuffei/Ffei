package com.example.zhuffei.ffei.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.adapter.GoodsAdapter;
import com.example.zhuffei.ffei.entity.Goods;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    private ImageView back;
    private ListView listView;
    private List<Goods> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActivity.this.finish();
            }
        });
        listView = (ListView) findViewById(R.id.listView);
        initData();
        listView.setAdapter(new GoodsAdapter(data,UserActivity.this));
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new Goods("电风扇",20.0,R.mipmap.btn_selected));
        data.add(new Goods("手电筒",20.0,R.mipmap.fabu));
        data.add(new Goods("自行车",20.0,R.mipmap.m9));
    }
}
