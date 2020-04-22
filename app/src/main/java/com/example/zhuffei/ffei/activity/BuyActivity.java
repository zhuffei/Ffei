package com.example.zhuffei.ffei.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.entity.Goods;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/3 12:49
 */
public class BuyActivity extends AppCompatActivity {

    private Goods data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
    }
}
