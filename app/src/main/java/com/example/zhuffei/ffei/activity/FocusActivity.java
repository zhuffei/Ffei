package com.example.zhuffei.ffei.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.adapter.UserAdapter;
import com.example.zhuffei.ffei.entity.User;

import java.util.ArrayList;
import java.util.List;

public class FocusActivity extends AppCompatActivity {

    public static final int FOCUS = 1;

    public static final int FANS = 2;

    private ListView listView;

    private List<User> data;

    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        listView = (ListView) findViewById(R.id.listView);
        initData();
        listView.setAdapter(new UserAdapter(this, data));
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FocusActivity.this.finish();
            }
        });
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new User(true, "R.mipmap.m4", "用户1"));

        data.add(new User(false, "R.mipmap.m5", "用户2"));
        data.add(new User(true, "R.mipmap.m6", "用户3"));

    }
}
