package com.example.zhuffei.ffei.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.zhuffei.ffei.R;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/3 12:49
 */
public class ReportActivity  extends AppCompatActivity{
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportActivity.this.finish();
            }
        });
    }
}