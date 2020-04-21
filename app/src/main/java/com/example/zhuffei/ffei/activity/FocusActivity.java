package com.example.zhuffei.ffei.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.adapter.UserAdapter;
import com.example.zhuffei.ffei.entity.User;

import java.util.ArrayList;
import java.util.List;
/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/3 12:49
 */
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
        listView = findViewById(R.id.listView);
        initData();
        listView.setAdapter(new UserAdapter(this, data));
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> FocusActivity.this.finish());
    }

    private void initData() {
        data = new ArrayList<>();

    }
}
